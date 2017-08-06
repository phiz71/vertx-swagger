package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //addPet
    void addPet(Pet body, User user, Handler<AsyncResult<Void>> handler);
    
    //deletePet
    void deletePet(Long petId, String apiKey, User user, Handler<AsyncResult<Void>> handler);
    
    //findPetsByStatus
    void findPetsByStatus(List<String> status, User user, Handler<AsyncResult<List<Pet>>> handler);
    
    //findPetsByTags
    void findPetsByTags(List<String> tags, User user, Handler<AsyncResult<List<Pet>>> handler);
    
    //getPetById
    void getPetById(Long petId, User user, Handler<AsyncResult<Pet>> handler);
    
    //updatePet
    void updatePet(Pet body, User user, Handler<AsyncResult<Void>> handler);
    
    //updatePetWithForm
    void updatePetWithForm(Long petId, String name, String status, User user, Handler<AsyncResult<Void>> handler);
    
    //uploadFile
    void uploadFile(Long petId, String additionalMetadata, File file, User user, Handler<AsyncResult<ModelApiResponse>> handler);
    
}
