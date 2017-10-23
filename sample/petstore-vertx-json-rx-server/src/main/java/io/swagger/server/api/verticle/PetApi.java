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

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

public interface PetApi  {
    //addPet
    Single<ResourceResponse<Void>> addPet(Pet body, User user);
    
    //deletePet
    Single<ResourceResponse<Void>> deletePet(Long petId, String apiKey, User user);
    
    //findPetsByStatus
    Single<ResourceResponse<JsonArray>> findPetsByStatus(JsonArray status, User user);
    
    //findPetsByTags
    Single<ResourceResponse<JsonArray>> findPetsByTags(JsonArray tags, User user);
    
    //getPetById
    Single<ResourceResponse<Pet>> getPetById(Long petId, User user);
    
    //updatePet
    Single<ResourceResponse<Void>> updatePet(Pet body, User user);
    
    //updatePetWithForm
    Single<ResourceResponse<Void>> updatePetWithForm(Long petId, String name, String status, User user);
    
    //uploadFile
    Single<ResourceResponse<ModelApiResponse>> uploadFile(Long petId, String additionalMetadata, File file, User user);
    
}
