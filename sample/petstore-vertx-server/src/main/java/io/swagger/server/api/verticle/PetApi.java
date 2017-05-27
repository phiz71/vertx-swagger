package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.vertx.core.Future;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //addPet
    public void addPet(Pet body);
    
    //deletePet
    public void deletePet(Long petId,String apiKey);
    
    //findPetsByStatus
    public Future<List<Pet>> findPetsByStatus(List<String> status);
    
    //findPetsByTags
    public Future<List<Pet>> findPetsByTags(List<String> tags);
    
    //getPetById
    public Future<Pet> getPetById(Long petId);
    
    //updatePet
    public void updatePet(Pet body);
    
    //updatePetWithForm
    public void updatePetWithForm(Long petId,String name,String status);
    
    //uploadFile
    public Future<ModelApiResponse> uploadFile(Long petId,String additionalMetadata,File file);
    
}
