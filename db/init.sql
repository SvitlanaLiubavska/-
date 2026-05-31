CREATE TABLE IF NOT EXISTS events (
    id          UUID PRIMARY KEY,
    payload     JSONB        NOT NULL,
    type        VARCHAR(100) NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_events_created_at
    ON events (created_at);

CREATE INDEX IF NOT EXISTS idx_events_created_at_type
    ON events (created_at, type);

CREATE INDEX IF NOT EXISTS idx_events_type
    ON events (type);


INSERT INTO events (id, payload, type, status)
VALUES (
  gen_random_uuid(),
  '{"type":"user.signup","userId":"u-12345","email":"alice@example.com","source":"web"}'::jsonb,
  'user.signup',
  'RECEIVED'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"user.signup","userId":"u-12345","email":"alice@example.com","source":"web"}'::jsonb,
  'user.signup',
  'RECEIVED',
  now() - INTERVAL '1 hour'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"user.signup","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'user.signup',
  'RECEIVED',
  now() - INTERVAL '1 day'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type1","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type1',
  'RECEIVED',
  now() - INTERVAL '3 hour'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type2","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type2',
  'RECEIVED',
  now() - INTERVAL '3 hour'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type2","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type2',
  'RECEIVED',
  now() - INTERVAL '12 hour'
);


INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type3","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type3',
  'RECEIVED',
  now() - INTERVAL '3 hour'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type4","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type4',
  'RECEIVED',
  now() - INTERVAL '3 hour'
);

INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type4","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type4',
  'RECEIVED',
  now() - INTERVAL '4 hour'
);
INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{"type":"type4","userId":"u-123456","email":"alice@example.com","source":"web"}'::jsonb,
  'type4',
  'RECEIVED',
  now() - INTERVAL '5 hour'
);


-- order.created
INSERT INTO events (id, payload, type, status, created_at)
VALUES (
  gen_random_uuid(),
  '{
    "type":"order.created",
    "order":{
      "id":"ord-998",
      "total":42.50,
      "items":[
        {"sku":"abc","qty":2},
        {"sku":"xyz","qty":1}
      ]
    }
  }'::jsonb,
  'order.created',
  'RECEIVED',
  now() - INTERVAL '1 day'
);


CREATE TABLE IF NOT EXISTS event_statistic_hourly (
    period_start TIMESTAMPTZ NOT NULL,
    type         VARCHAR(100) NOT NULL,
    count        BIGINT NOT NULL,
    PRIMARY KEY (period_start, type)
);

CREATE INDEX IF NOT EXISTS idx_event_statistic_hourly_period_start
    ON event_statistic_hourly(period_start);

CREATE INDEX IF NOT EXISTS idx_event_statistic_hourly_period_start_type
    ON event_statistic_hourly(period_start, type);



INSERT INTO event_statistic_hourly (period_start, type, count)
SELECT
    date_trunc('hour', created_at) AS period_start,
    type,
    COUNT(*) AS count
FROM events
GROUP BY date_trunc('hour', created_at), type
ON CONFLICT (period_start, type)
DO UPDATE SET count = EXCLUDED.count;