## Implementation Overview

### Scope

For the Java implementation, I focused on the following components:

* **Read API**
* **Statistics**

Authentication was skipped.

For the Python implementation, I focused on the following components:

* **Control panel: health & status**

Rest was skipped.

---

### Database Changes

To improve performance and simplify queries, the following changes were introduced:

* Extracted the **event type** into a separate column to avoid JSON parsing during queries.
* Added a new table: **`event_statistic_hourly`** for storing pre-aggregated statistics.
* Add a new schema: **`event_processor`** and a table  **`event_processing_status`** for python part

---

### Pagination

The maximum page size is limited to **100 records**. This provides a balance between usability and preventing excessively heavy database queries.

---

### Statistics Design Considerations

The structure of the statistics API depends heavily on the use case. For example, in a **dashboard context**, it is preferable to:

* Provide **separate endpoints for each metric**
* Avoid blocking the entire dashboard by allowing data to load **widget by widget**

---

### Data Sources

Two data sources are used for statistics:

1. **`event_statistic_hourly`** – for aggregated data (grouped by type and hour)
2. **`event` table** – for real-time data covering the most recent period

If query performance becomes an issue, the real-time component can be removed. This would result in slightly stale data (up to 1 hour delay), but significantly improve performance.

---

### Performance & Monitoring

Basic logging has been added to track database queries and identify potential bottlenecks.

---

### Aggregation Strategy

Currently, the `event_statistic_hourly` table is updated **on each insert** into the `event` table.

In high-load scenarios, this approach may lead to performance degradation. As an alternative, I suggest:

* Switching to an **hourly scheduled job** that aggregates and updates statistics in batches

This would reduce write pressure on the system and improve overall stability.

---

### Control panel event status

The current implementation does not rely on Kafka offsets or MinIO metadata to track processing state.
Instead, it uses a **database-driven state model**.

This design was chosen because:

- Kafka offsets only guarantee consumption order, not business-level processing state
- MinIO does not provide a queryable index for event metadata by default
- A relational store provides reliable lookup for:
   - event status (`pending`, `processed`)
   - processing timestamp

The relationship is explicitly stored via:

- `event_id` → used as the **primary key**
- MinIO object key → derived deterministically from event_id

**Currant flow**

1. Consume event from Kafka topic `events`
2. Extract `event_id` from message
3. Store initial status in PostgreSQL:
    - `pending` before processing
4. Transform JSON payload → XML
5. Store XML file in MinIO bucket `events`
    - filename format: `{event_id}.xml`
6. Update event status to:
    - `processed`
    - store `processed_at` timestamp
7. Get currant status via /events/{id}/status

---

### Replay

For  `POST /events/{id}/replay` endpoint:

- Event payload will be retrieved from `event-api`
  - this keeps Python and Java storage concerns separated
  - python service does not duplicate event storage logic
  - java service remains the single source of truth for event payloads
- Before processing, the system checks `event_processing_status`
- If event is already marked as `processed`, replay logic will:
   - avoid duplicate MinIO writes
   - either skip or explicitly overwrite based on design choice

