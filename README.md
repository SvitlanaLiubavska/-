## Implementation Overview

### Scope

For the Java implementation, I focused on the following components:

* **Read API**
* **Statistics**

Authentication was skipped.

---

### Database Changes

To improve performance and simplify queries, the following changes were introduced:

* Extracted the **event type** into a separate column to avoid JSON parsing during queries.
* Added a new table: **`event_statistic_hourly`** for storing pre-aggregated statistics.

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

