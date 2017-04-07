package com.github.phiz71.vertx.swagger.router;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import org.apache.commons.lang3.StringUtils;

/**
 * Implementation of {@link ServiceIdResolver} which uses {@code operationId} property specified for an operation in Swagger file
 * and falls back to {@link DefaultServiceIdResolver} if no operation ID specified.
 *
 * @since 12/02/2017.
 */
public class OperationIdServiceIdResolver extends DefaultServiceIdResolver {
    @Override
    public String resolve(HttpMethod httpMethod, String pathname, Operation operation) {
        if (StringUtils.isBlank(operation.getOperationId())) {
            return super.resolve(httpMethod, pathname, operation);
        }

        return operation.getOperationId();
    }
}
