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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;

public interface PetApi  {
    //addPet
    void addPet(Pet body, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler);
    
    //deletePet
    void deletePet(Long petId, String apiKey, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler);
    
    //findPetsByStatus
    void findPetsByStatus(JsonArray status, User user, Handler<AsyncResult<ResourceResponse<JsonArray>>> handler);
    
    //findPetsByTags
    void findPetsByTags(JsonArray tags, User user, Handler<AsyncResult<ResourceResponse<JsonArray>>> handler);
    
    //getPetById
    void getPetById(Long petId, User user, Handler<AsyncResult<ResourceResponse<Pet>>> handler);
    
    //updatePet
    void updatePet(Pet body, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler);
    
    //updatePetWithForm
    void updatePetWithForm(Long petId, String name, String status, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler);
    
    //uploadFile
    void uploadFile(Long petId, String additionalMetadata, File file, User user, Handler<AsyncResult<ResourceResponse<ModelApiResponse>>> handler);
    
}
