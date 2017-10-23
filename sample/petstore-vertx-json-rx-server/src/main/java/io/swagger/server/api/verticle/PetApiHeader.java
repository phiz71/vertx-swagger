package io.swagger.server.api.verticle;

import java.io.File;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public final class PetApiHeader extends MainApiHeader {
    private PetApiHeader(String name, String value) {
        super(name, value);
    }
    
    private PetApiHeader(String name, Iterable<String> values) {
        super(name, values);
    }
    
    

}