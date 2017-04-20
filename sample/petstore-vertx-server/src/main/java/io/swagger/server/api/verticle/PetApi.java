package io.swagger.server.api.verticle;

import java.io.File;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;

import java.util.List;
import java.util.Map;

public interface PetApi  {
    //addPet
    public void addPet(Pet body) throws PetApiException;
    
    //deletePet
    public void deletePet(Long petId,String apiKey) throws PetApiException;
    
    //findPetsByStatus
    public List<Pet> findPetsByStatus(List<String> status) throws PetApiException;
    
    //findPetsByTags
    public List<Pet> findPetsByTags(List<String> tags) throws PetApiException;
    
    //getPetById
    public Pet getPetById(Long petId) throws PetApiException;
    
    //updatePet
    public void updatePet(Pet body) throws PetApiException;
    
    //updatePetWithForm
    public void updatePetWithForm(Long petId,String name,String status) throws PetApiException;
    
    //uploadFile
    public ModelApiResponse uploadFile(Long petId,String additionalMetadata,File file) throws PetApiException;
    
}
