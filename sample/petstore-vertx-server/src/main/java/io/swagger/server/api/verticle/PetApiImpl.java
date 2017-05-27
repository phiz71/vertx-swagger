package io.swagger.server.api.verticle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.Pet.StatusEnum;
import io.vertx.core.Future;

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
    public Future<List<Pet>> findPetsByStatus(List<String> status) {
        Future<List<Pet>> futureResult = Future.future();
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(),new ArrayList<>(), StatusEnum.AVAILABLE));
        futureResult.complete(pets);
        return futureResult;
    }

    @Override
    public Future<List<Pet>> findPetsByTags(List<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Pet> getPetById(Long petId) {
        Future<Pet> futureResult = Future.future();
        if(petId.equals(1L))
            futureResult.complete(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(),new ArrayList<>(), StatusEnum.AVAILABLE));
        else
            futureResult.complete(null);
        return futureResult;
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
    public Future<ModelApiResponse> uploadFile(Long petId, String additionalMetadata, File file) {
        // TODO Auto-generated method stub
        return null;
    }

}
