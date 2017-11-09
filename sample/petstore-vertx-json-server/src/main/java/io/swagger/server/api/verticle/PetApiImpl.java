package io.swagger.server.api.verticle;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.auth.User;

import java.io.File;

public class PetApiImpl implements PetApi {

    @Override
    public void addPet(Pet body, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deletePet(Long petId, String apiKey, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void findPetsByStatus(JsonArray status, User user, Handler<AsyncResult<ResourceResponse<JsonArray>>> handler) {
        JsonArray pets = new JsonArray();
        pets.add(new Pet(1L, new Category(1L, "dog"), "rex", new JsonArray(),new JsonArray(), Pet.PetStatus.AVAILABLE));
        
        ResourceResponse<JsonArray> response = new ResourceResponse<>();
        response.setResponse(pets);
        response.addHeader(PetApiHeader.CONTENT_TYPE_JSON);
        handler.handle(Future.succeededFuture(response));
    }

    @Override
    public void findPetsByTags(JsonArray tags, User user, Handler<AsyncResult<ResourceResponse<JsonArray>>> handler) {
        // TODO Auto-generated method stub
        handler.handle(Future.succeededFuture(null));
    }

    @Override
    public void getPetById(Long petId, User user, Handler<AsyncResult<ResourceResponse<Pet>>> handler) {
        if(petId.equals(1L)) {
            ResourceResponse<Pet> response = new ResourceResponse<>();
            response.setResponse(new Pet(1L, new Category(1L, "dog"), "rex", new JsonArray(),new JsonArray(), Pet.PetStatus.AVAILABLE));
            response.addHeader(PetApiHeader.CONTENT_TYPE_JSON);
            handler.handle(Future.succeededFuture(response));
            return;
        }
        else if (petId.equals(2L))
             handler.handle(Future.failedFuture(new NullPointerException("simulation")));

        handler.handle(Future.failedFuture(PetApiException.PetApi_getPetById_404_createException()));
        
    }

    @Override
    public void updatePet(Pet body, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePetWithForm(Long petId, String name, String status, User user, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void uploadFile(Long petId, String additionalMetadata, File file, User user, Handler<AsyncResult<ResourceResponse<ModelApiResponse>>> handler) {
        // TODO Auto-generated method stub

    }

}
