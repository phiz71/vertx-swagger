package com.github.phiz71.vertx.swagger.router;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;

/**
 * Default implementation of {@link ServiceIdResolver} which generates service ID by concatenating HTTP verb and endpoint path.
 *
 * <p>Example:
 *
 * <p>Endpoint for the operation
 *
 * <pre>POST /withGeneratedServiceId/{param1}/and/path</pre>
 *
 * is converted to service ID
 *
 * <pre>POST_withGeneratedServiceId_param1_and_path</pre>
 *
 * @since 12/02/2017.
 */
public class DefaultServiceIdResolver implements ServiceIdResolver {
    @Override
    public String resolve(HttpMethod httpMethod, String pathname, Operation operation) {
        return httpMethod.name() + pathname.replaceAll("-", "_").replaceAll("/", "_").replaceAll("[{}]", "");
    }
}
