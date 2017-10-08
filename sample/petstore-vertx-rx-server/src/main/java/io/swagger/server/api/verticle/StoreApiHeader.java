package io.swagger.server.api.verticle;

import io.swagger.server.api.util.MainApiException;
import io.swagger.server.api.util.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;

public final class StoreApiHeader extends MainApiHeader {
    private StoreApiHeader(String name, String value) {
        super(name, value);
    }
    
    private StoreApiHeader(String name, Iterable<String> values) {
        super(name, values);
    }
    
    

}