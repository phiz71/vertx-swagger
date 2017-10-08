package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.util.MainApiException;
import io.swagger.server.api.util.MainApiHeader;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.util.ResourceResponse;

public final class PetApiHeader extends MainApiHeader {
    private PetApiHeader(String name, String value) {
        super(name, value);
    }
    
    private PetApiHeader(String name, Iterable<String> values) {
        super(name, values);
    }
    
    

}