package com.example.eventapi.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class FormattedLogger {
    public void logPerf(String className, String method, String operation, Map<String, Object> parameters, long durationMs) {
        log.info("traceId={} class={} method={} operation={} durationMs={} params={}",
                TraceIdUtil.getOrCreate(),
                className,
                method,
                operation,
                durationMs,
                parameters
        );
    }
}
