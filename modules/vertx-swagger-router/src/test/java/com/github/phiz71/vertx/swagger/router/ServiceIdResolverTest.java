package com.github.phiz71.vertx.swagger.router;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import org.junit.Test;

import com.github.phiz71.vertx.swagger.router.DefaultServiceIdResolver;
import com.github.phiz71.vertx.swagger.router.OperationIdServiceIdResolver;
import com.github.phiz71.vertx.swagger.router.ServiceIdResolver;

import static org.junit.Assert.assertEquals;

/**
 * Created by inikolaev on 12/02/2017.
 */
public class ServiceIdResolverTest {
    @Test
    public void testDefaultResolver() {
        final ServiceIdResolver resolver = new DefaultServiceIdResolver();
        final String serviceId = resolver.resolve(HttpMethod.GET, "/some/path", new Operation());

        assertEquals("GET_some_path", serviceId);
    }

    @Test
    public void testDefaultResolverWithParameters() {
        final ServiceIdResolver resolver = new DefaultServiceIdResolver();
        final String serviceId = resolver.resolve(HttpMethod.GET, "/some/path/{with}/{multiple}/parameters", new Operation());

        assertEquals("GET_some_path_with_multiple_parameters", serviceId);
    }

    @Test
    public void testDefaultResolverParameterWithDash() {
        final ServiceIdResolver resolver = new DefaultServiceIdResolver();
        final String serviceId = resolver.resolve(HttpMethod.GET, "/some/path/{with-dash}/parameter", new Operation());

        assertEquals("GET_some_path_with_dash_parameter", serviceId);
    }

    @Test
    public void testOperationIdResolver() {
        final ServiceIdResolver resolver = new OperationIdServiceIdResolver();
        final String serviceId = resolver.resolve(HttpMethod.GET, "/some/path", new Operation().operationId("testOperationIdResolver"));

        assertEquals("testOperationIdResolver", serviceId);
    }

    @Test
    public void testOperationIdResolverFallback() {
        final ServiceIdResolver resolver = new OperationIdServiceIdResolver();
        final String serviceId = resolver.resolve(HttpMethod.GET, "/some/path/{with}/{multiple}/parameters/fallback", new Operation());

        assertEquals("GET_some_path_with_multiple_parameters_fallback", serviceId);
    }
}
