from fastapi import APIRouter, HTTPException
import psycopg2
import uuid


router = APIRouter()


from config import (
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

@router.get("/events/{event_id}/status")
def get_event_status(event_id: str):
    conn = get_conn()
    try:
        try:
            event_uuid = uuid.UUID(event_id)
        except ValueError:
            raise HTTPException(status_code=400, detail="Invalid UUID")

        with conn.cursor() as cur:
            cur.execute("""
                        SELECT status, processed_at
                        FROM event_processor.event_processing_status
                        WHERE event_id = %s
                        """, (str(event_uuid),))

            row = cur.fetchone()

        if not row:
            raise HTTPException(status_code=404, detail="Event not found")

        status, processed_at = row

        return {
            "status": status,
            "processedAt": processed_at
        }

    finally:
        conn.close()