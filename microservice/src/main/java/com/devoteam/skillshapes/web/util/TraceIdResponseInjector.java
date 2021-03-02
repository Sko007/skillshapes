package com.devoteam.skillshapes.web.util;

import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.*;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;

/**
 * @Author Devoteam
 * @Description To debug an Ajax request more easily return the traceId in response header.
 */
@Provider
public class TraceIdResponseInjector implements ContainerResponseFilter {

    @Inject
    Tracer tracer;

    private static final String TRACE_ID = "Trace-ID";

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {
        Optional.ofNullable(tracer)
            .map(Tracer::scopeManager)
            .map(ScopeManager::active)
            .map(Scope::span)
            .map(Span::context)
            .filter(spanContext -> spanContext instanceof JaegerSpanContext)
            .ifPresent(spanContext -> responseContext.getHeaders().add(TRACE_ID, ((JaegerSpanContext) spanContext).getTraceId()));
    }
}
