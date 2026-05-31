#  event-processor (Python)

A service that consumes events from Kafka, stores files in MinIO, and exposes an HTTP API for health checks.

---

## Run infrastructure (Kafka + MinIO + Postgres)

docker compose up -d

---

## Install dependencies

pip install fastapi uvicorn confluent-kafka minio

---

## Start Api

uvicorn app:app --reload --port 8000

---

## Start main.py

pip install -r requirements.txt
pip install psycopg2-binary
python main.py

---

## Stop

Ctrl + c



