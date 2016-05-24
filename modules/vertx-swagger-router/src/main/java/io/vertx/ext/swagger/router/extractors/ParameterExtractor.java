package io.vertx.ext.swagger.router.extractors;

import io.swagger.models.parameters.Parameter;
import io.vertx.ext.web.RoutingContext;

public interface ParameterExtractor {
    Object extract(String name, Parameter parameter, RoutingContext context);
}
