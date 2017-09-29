package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //addPet
    Completable addPet(Pet body, User user);
    
    //deletePet
    Completable deletePet(Long petId, String apiKey, User user);
    
    //findPetsByStatus
    Single<List<Pet>> findPetsByStatus(List<String> status, User user);
    
    //findPetsByTags
    Single<List<Pet>> findPetsByTags(List<String> tags, User user);
    
    //getPetById
    Single<Pet> getPetById(Long petId, User user);
    
    //updatePet
    Completable updatePet(Pet body, User user);
    
    //updatePetWithForm
    Completable updatePetWithForm(Long petId, String name, String status, User user);
    
    //uploadFile
    Single<ModelApiResponse> uploadFile(Long petId, String additionalMetadata, File file, User user);
    
}
