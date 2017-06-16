package io.swagger.server.api.verticle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.Pet.StatusEnum;
import rx.Completable;
import rx.Single;
import rx.functions.Func1;

public class PetApiImpl implements PetApi {

    @Override
    public Completable addPet(Pet body) {
        return Completable.complete();
    }

    @Override
    public Completable deletePet(Long petId, String apiKey) {
        return Completable.complete();
    }

    @Override
    public Single<List<Pet>> findPetsByStatus(List<String> status) {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(), new ArrayList<>(), StatusEnum.AVAILABLE));

        return Single.just(pets);
    }

    @Override
    public Single<List<Pet>> findPetsByTags(List<String> tags) {
        return Single.just(new ArrayList<>());
    }

    @Override
    public Single<Pet> getPetById(Long petId) {
        return Single.just(petId).flatMap(new Func1<Long, Single<Pet>>() {
            @Override
            public Single<Pet> call(Long id) {
                if (id.equals(1L))
                    return Single.just(new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(), new ArrayList<>(), StatusEnum.AVAILABLE));
                else if (id.equals(2L))
                    return Single.error(new NullPointerException("simulation"));
                else
                    return Single.error(PetApiException.Pet_getPetById_404_Exception);
            }
        });

    }

    @Override
    public Completable updatePet(Pet body) {
        return Completable.complete();
    }

    @Override
    public Completable updatePetWithForm(Long petId, String name, String status) {
        return Completable.complete();
    }

    @Override
    public Single<ModelApiResponse> uploadFile(Long petId, String additionalMetadata, File file) {
        return Single.just(new ModelApiResponse());
    }

}
