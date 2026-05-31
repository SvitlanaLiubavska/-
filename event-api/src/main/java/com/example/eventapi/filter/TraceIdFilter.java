package com.example.eventapi.filter;

import com.example.eventapi.util.TraceIdUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class TraceIdFilter implements Filter {
    private static final String HEADER = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest http = (HttpServletRequest) request;

            String traceId = http.getHeader(HEADER);

            if (traceId == null || traceId.isBlank()) {
                TraceIdUtil.getOrCreate();
            } else {
                TraceIdUtil.set(traceId);
            }

            chain.doFilter(request, response);

        } finally {
            TraceIdUtil.clear();
        }
    }
}
