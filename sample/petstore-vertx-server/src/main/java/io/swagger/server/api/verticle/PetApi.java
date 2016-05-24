package io.swagger.server.api.verticle;

import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.ModelApiResponse;
import java.io.File;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //POST_pet
    public void addPet(Pet body);
    
    //DELETE_pet_petId
    public void deletePet(Long petId,String apiKey);
    
    //GET_pet_findByStatus
    public List<Pet> findPetsByStatus(List<String> status);
    
    //GET_pet_findByTags
    public List<Pet> findPetsByTags(List<String> tags);
    
    //GET_pet_petId
    public Pet getPetById(Long petId);
    
    //PUT_pet
    public void updatePet(Pet body);
    
    //POST_pet_petId
    public void updatePetWithForm(Long petId,String name,String status);
    
    //POST_pet_petId_uploadImage
    public ModelApiResponse uploadFile(Long petId,String additionalMetadata,File file);
    
}
