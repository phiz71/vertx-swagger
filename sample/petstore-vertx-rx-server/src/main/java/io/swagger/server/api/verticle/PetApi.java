package io.swagger.server.api.verticle;


import java.io.File;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import rx.Single;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //addPet
    public Single<Void> addPet(Pet body);
    
    //deletePet
    public Single<Void> deletePet(Long petId,String apiKey);
    
    //findPetsByStatus
    public Single<List<Pet>> findPetsByStatus(List<String> status);
    
    //findPetsByTags
    public Single<List<Pet>> findPetsByTags(List<String> tags);
    
    //getPetById
    public Single<Pet> getPetById(Long petId);
    
    //updatePet
    public Single<Void> updatePet(Pet body);
    
    //updatePetWithForm
    public Single<Void> updatePetWithForm(Long petId,String name,String status);
    
    //uploadFile
    public Single<ModelApiResponse> uploadFile(Long petId,String additionalMetadata,File file);
    
}
