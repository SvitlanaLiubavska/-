package com.example.eventapi.util;

import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdUtil {
    public static final String TRACE_ID = "traceId";

    public static String getOrCreate() {
        String traceId = MDC.get(TRACE_ID);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put(TRACE_ID, traceId);
        }
        return traceId;
    }

    public static void set(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static void clear() {
        MDC.remove(TRACE_ID);
    }
}
