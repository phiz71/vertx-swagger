package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public final class StoreApiHeader extends MainApiHeader {
    private StoreApiHeader(String name, String value) {
        super(name, value);
    }
    
    private StoreApiHeader(String name, Iterable<String> values) {
        super(name, values);
    }
    
    

}