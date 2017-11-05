package io.swagger.server.api.verticle;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.ext.auth.User;
import rx.Single;
import rx.functions.Func1;

import java.io.File;

public class PetApiImpl implements PetApi {

    @Override
    public Single<ResourceResponse<Void>> addPet(Pet body, User user) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<Void>> deletePet(Long petId, String apiKey, User user) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<JsonArray>> findPetsByStatus(JsonArray status, User user) {
        JsonArray pets = new JsonArray();
        pets.add(new Pet(1L, new Category(1L, "dog"), "rex", new JsonArray(), new JsonArray(), Pet.PetStatus.AVAILABLE));

        ResourceResponse<JsonArray> response = new ResourceResponse<>();
        response.setResponse(pets);
        response.addHeader(PetApiHeader.CONTENT_TYPE_JSON);
        
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<JsonArray>> findPetsByTags(JsonArray tags, User user) {
        return Single.just(null);
    }

    @Override
    public Single<ResourceResponse<Pet>> getPetById(Long petId, User user) {
        return Single.just(petId).flatMap((Func1<Long, Single<ResourceResponse<Pet>>>) id -> {
            if (id.equals(1L)) {
                ResourceResponse<Pet> response = new ResourceResponse<>();
                response.setResponse(new Pet(1L, new Category(1L, "dog"), "rex", new JsonArray(), new JsonArray(), Pet.PetStatus.AVAILABLE));
                response.addHeader(PetApiHeader.CONTENT_TYPE_JSON);
                return Single.just(response);
            }
            else if (id.equals(2L))
                return Single.error(new NullPointerException("simulation"));
            else
                return Single.error(PetApiException.PetApi_getPetById_404_createException());
        });

    }

    @Override
    public Single<ResourceResponse<Void>> updatePet(Pet body, User user) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<Void>> updatePetWithForm(Long petId, String name, String status, User user) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<ModelApiResponse>> uploadFile(Long petId, String additionalMetadata, File file, User user) {
        ResourceResponse<ModelApiResponse> response = new ResourceResponse<>();
        response.addHeader(PetApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new ModelApiResponse());
        return Single.just(response);
    }

    private Single<ResourceResponse<Void>> returnVoid() {
        ResourceResponse<Void> response = new ResourceResponse<>();
        response.addHeader(PetApiHeader.CONTENT_TYPE_JSON);
        return Single.just(response);
    }
}
