package io.swagger.server.api.verticle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.Pet.StatusEnum;

public class PetApiImpl implements PetApi {

    @Override
    public void addPet(Pet body) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deletePet(Long petId, String apiKey) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Pet> findPetsByStatus(List<String> status) {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(),new ArrayList<>(), StatusEnum.AVAILABLE));
        return pets;
    }

    @Override
    public List<Pet> findPetsByTags(List<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pet getPetById(Long petId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updatePet(Pet body) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePetWithForm(Long petId, String name, String status) {
        // TODO Auto-generated method stub

    }

    @Override
    public ModelApiResponse uploadFile(Long petId, String additionalMetadata, File file) {
        // TODO Auto-generated method stub
        return null;
    }

}
