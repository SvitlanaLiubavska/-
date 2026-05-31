"""
event-processor (Python)

Consumes events from a Kafka topic, transforms each JSON payload into XML,
and stores the result in a Minio bucket.
"""

import json
import sys
import uuid
from io import BytesIO

import xmltodict
from confluent_kafka import Consumer
from minio import Minio
import psycopg2

from config import (
    KAFKA_BOOTSTRAP,
    MINIO_ENDPOINT,
    MINIO_ACCESS_KEY,
    MINIO_SECRET_KEY,
    TOPIC,
    BUCKET,
    GROUP_ID,
    POSTGRES_HOST,
    POSTGRES_PORT,
    POSTGRES_DB,
    POSTGRES_USER,
    POSTGRES_PASSWORD
)

def get_conn():
    return psycopg2.connect(
        host=POSTGRES_HOST,
        port=POSTGRES_PORT,
        database=POSTGRES_DB,
        user=POSTGRES_USER,
        password=POSTGRES_PASSWORD
    )

def mark_pending(conn, event_id: str):
    try:
        with conn.cursor() as cur:
            cur.execute("""
                        INSERT INTO event_processor.event_processing_status(event_id, status)
                        VALUES (%s, 'pending')
                            ON CONFLICT (event_id) DO NOTHING
                        """, (event_id,))
    except Exception as e:
        print("DB ERROR (pending):", e)
        raise

def mark_processed(conn, event_id: str):
    try:
        with conn.cursor() as cur:
            cur.execute("""
                        UPDATE event_processor.event_processing_status
                        SET status = 'processed',
                            processed_at = NOW()
                        WHERE event_id = %s
                        """, (event_id,))
    except Exception as e:
        print("DB ERROR (processed):", e)
        raise


def ensure_bucket(client):
    if not client.bucket_exists(BUCKET):
        client.make_bucket(BUCKET)
        print(f'Created bucket "{BUCKET}"', flush=True)


def main():
    minio_client = Minio(
        MINIO_ENDPOINT,
        access_key=MINIO_ACCESS_KEY,
        secret_key=MINIO_SECRET_KEY,
        secure=False,
    )
    ensure_bucket(minio_client)

    consumer = Consumer({
        "bootstrap.servers": KAFKA_BOOTSTRAP,
        "group.id": GROUP_ID,
        "auto.offset.reset": "earliest",
    })
    consumer.subscribe([TOPIC])

    while True:
        msg = consumer.poll(timeout=1.0)
        if msg is None:
            continue
        if msg.error():
            print(f"Consumer error: {msg.error()}", file=sys.stderr, flush=True)
            continue

        data = json.loads(msg.value().decode("utf-8"))
        event_id = data.get("id")

        if not event_id:
            print("NO EVENT ID, skipping")
            continue

        xml = xmltodict.unparse({"event": data}, pretty=True)

        filename = f"{event_id}.xml"
        body = xml.encode("utf-8")

        conn = get_conn()
        mark_pending(conn, event_id)
        conn.commit()

        minio_client.put_object(BUCKET, filename, BytesIO(body), length=len(body))

        mark_processed(conn, event_id)
        conn.commit()
        conn.close()

        print(f"Saved {filename}", flush=True)


if __name__ == "__main__":
    main()
