package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //addPet
    public void addPet(Pet body);
    
    //deletePet
    public void deletePet(Long petId,String apiKey);
    
    //findPetsByStatus
    public List<Pet> findPetsByStatus(List<String> status);
    
    //findPetsByTags
    public List<Pet> findPetsByTags(List<String> tags);
    
    //getPetById
    public Pet getPetById(Long petId);
    
    //updatePet
    public void updatePet(Pet body);
    
    //updatePetWithForm
    public void updatePetWithForm(Long petId,String name,String status);
    
    //uploadFile
    public ModelApiResponse uploadFile(Long petId,String additionalMetadata,File file);
    
}
