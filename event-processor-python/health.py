from fastapi import APIRouter, HTTPException
from confluent_kafka import Consumer
from minio import Minio

router = APIRouter()

from config import (
    KAFKA_BOOTSTRAP,
    MINIO_ENDPOINT,
    MINIO_ACCESS_KEY,
    MINIO_SECRET_KEY,
    GROUP_ID
)

kafka_consumer = Consumer({
    "bootstrap.servers": KAFKA_BOOTSTRAP,
    "group.id": GROUP_ID,
    "auto.offset.reset": "earliest",
})

minio_client = Minio(
    MINIO_ENDPOINT,
    access_key=MINIO_ACCESS_KEY,
    secret_key=MINIO_SECRET_KEY,
    secure=False,
)

@router.get("/health")
def health():
    checks = {}

    try:
        metadata = kafka_consumer.list_topics(timeout=3)
        if metadata.topics:
            checks["kafka"] = "ok"
        else:
            checks["kafka"] = "fail: no topics"
    except Exception as e:
        checks["kafka"] = f"fail: {str(e)}"

    try:
        minio_client.list_buckets()
        checks["minio"] = "ok"
    except Exception as e:
        checks["minio"] = f"fail: {str(e)}"

    if all(v == "ok" for v in checks.values()):
        return {
            "status": "healthy",
            "checks": checks
        }

    raise HTTPException(
        status_code=503,
        detail={
            "status": "unhealthy",
            "checks": checks
        }
    )