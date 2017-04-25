package com.github.phiz71.vertx.swagger.router;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;

/**
 * Created by inikolaev on 12/02/2017.
 */
public interface ServiceIdResolver {
    String resolve(HttpMethod httpMethod, String pathname, Operation operation);
}
