package io.swagger.server.api.verticle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.Pet.StatusEnum;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class PetApiImpl implements PetApi {

    @Override
    public void addPet(Pet body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deletePet(Long petId, String apiKey, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void findPetsByStatus(List<String> status, Handler<AsyncResult<List<Pet>>> handler) {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(),new ArrayList<>(), StatusEnum.AVAILABLE));
        handler.handle(Future.succeededFuture(pets));
    }

    @Override
    public void findPetsByTags(List<String> tags, Handler<AsyncResult<List<Pet>>> handler) {
        // TODO Auto-generated method stub
        handler.handle(Future.succeededFuture(null));
    }

    @Override
    public void getPetById(Long petId, Handler<AsyncResult<Pet>> handler) {
        if(petId.equals(1L)) {
            handler.handle(Future.succeededFuture(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(),new ArrayList<>(), StatusEnum.AVAILABLE)));
            return;
        }
        else if (petId.equals(2L))
             handler.handle(Future.failedFuture(new NullPointerException("simulation")));

        handler.handle(Future.failedFuture(PetApiException.Pet_getPetById_404_Exception));
        
    }

    @Override
    public void updatePet(Pet body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePetWithForm(Long petId, String name, String status, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void uploadFile(Long petId, String additionalMetadata, File file, Handler<AsyncResult<ModelApiResponse>> handler) {
        // TODO Auto-generated method stub

    }

}
