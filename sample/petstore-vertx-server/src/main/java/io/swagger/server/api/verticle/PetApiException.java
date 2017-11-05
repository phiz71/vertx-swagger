package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public final class PetApiException extends MainApiException {
    public PetApiException(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }
    
    public static PetApiException PetApi_addPet_405_createException() {
        return new PetApiException(405, "Invalid input");
    }
    public static PetApiException PetApi_deletePet_400_createException() {
        return new PetApiException(400, "Invalid ID supplied");
    }
    public static PetApiException PetApi_deletePet_404_createException() {
        return new PetApiException(404, "Pet not found");
    }
    public static PetApiException PetApi_findPetsByStatus_400_createException() {
        return new PetApiException(400, "Invalid status value");
    }
    public static PetApiException PetApi_findPetsByTags_400_createException() {
        return new PetApiException(400, "Invalid tag value");
    }
    public static PetApiException PetApi_getPetById_400_createException() {
        return new PetApiException(400, "Invalid ID supplied");
    }
    public static PetApiException PetApi_getPetById_404_createException() {
        return new PetApiException(404, "Pet not found");
    }
    public static PetApiException PetApi_updatePet_400_createException() {
        return new PetApiException(400, "Invalid ID supplied");
    }
    public static PetApiException PetApi_updatePet_404_createException() {
        return new PetApiException(404, "Pet not found");
    }
    public static PetApiException PetApi_updatePet_405_createException() {
        return new PetApiException(405, "Validation exception");
    }
    public static PetApiException PetApi_updatePetWithForm_405_createException() {
        return new PetApiException(405, "Invalid input");
    }
    

}