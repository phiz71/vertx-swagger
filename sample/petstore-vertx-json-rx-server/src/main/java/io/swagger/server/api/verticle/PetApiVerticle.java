package io.swagger.server.api.verticle;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.ext.auth.User;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import java.io.File;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiFactory;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public class PetApiVerticle extends AbstractVerticle {
    private VerticleHelper verticleHelper = new VerticleHelper(this.getClass());

    private static final String ADDPET_SERVICE_ID = "addPet";
    private static final String DELETEPET_SERVICE_ID = "deletePet";
    private static final String FINDPETSBYSTATUS_SERVICE_ID = "findPetsByStatus";
    private static final String FINDPETSBYTAGS_SERVICE_ID = "findPetsByTags";
    private static final String GETPETBYID_SERVICE_ID = "getPetById";
    private static final String UPDATEPET_SERVICE_ID = "updatePet";
    private static final String UPDATEPETWITHFORM_SERVICE_ID = "updatePetWithForm";
    private static final String UPLOADFILE_SERVICE_ID = "uploadFile";
    

    //Handler for addPet
    private final Handler<Message<JsonObject>> addPetHandler;
    //Handler for deletePet
    private final Handler<Message<JsonObject>> deletePetHandler;
    //Handler for findPetsByStatus
    private final Handler<Message<JsonObject>> findPetsByStatusHandler;
    //Handler for findPetsByTags
    private final Handler<Message<JsonObject>> findPetsByTagsHandler;
    //Handler for getPetById
    private final Handler<Message<JsonObject>> getPetByIdHandler;
    //Handler for updatePet
    private final Handler<Message<JsonObject>> updatePetHandler;
    //Handler for updatePetWithForm
    private final Handler<Message<JsonObject>> updatePetWithFormHandler;
    //Handler for uploadFile
    private final Handler<Message<JsonObject>> uploadFileHandler;
    

    public PetApiVerticle(PetApi service) {
    
     addPetHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            Pet body = new Pet(message.body().getJsonObject("body"));
                service.addPet(body, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, false, new TypeReference<Void>(){}),
                    verticleHelper.getErrorAction(message, ADDPET_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, ADDPET_SERVICE_ID);
        }
    };
    
     deletePetHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
            String apiKey = message.body().getString("api_key");
                service.deletePet(petId, apiKey, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, false, new TypeReference<Void>(){}),
                    verticleHelper.getErrorAction(message, DELETEPET_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, DELETEPET_SERVICE_ID);
        }
    };
    
     findPetsByStatusHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            JsonArray status = message.body().getJsonArray("status");
                service.findPetsByStatus(status, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, true, new TypeReference<JsonArray>(){}),
                    verticleHelper.getErrorAction(message, FINDPETSBYSTATUS_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, FINDPETSBYSTATUS_SERVICE_ID);
        }
    };
    
     findPetsByTagsHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            JsonArray tags = message.body().getJsonArray("tags");
                service.findPetsByTags(tags, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, true, new TypeReference<JsonArray>(){}),
                    verticleHelper.getErrorAction(message, FINDPETSBYTAGS_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, FINDPETSBYTAGS_SERVICE_ID);
        }
    };
    
     getPetByIdHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                service.getPetById(petId, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, true, new TypeReference<Pet>(){}),
                    verticleHelper.getErrorAction(message, GETPETBYID_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, GETPETBYID_SERVICE_ID);
        }
    };
    
     updatePetHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            Pet body = new Pet(message.body().getJsonObject("body"));
                service.updatePet(body, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, false, new TypeReference<Void>(){}),
                    verticleHelper.getErrorAction(message, UPDATEPET_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, UPDATEPET_SERVICE_ID);
        }
    };
    
     updatePetWithFormHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
            String name = message.body().getString("name");
            String status = message.body().getString("status");
                service.updatePetWithForm(petId, name, status, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, false, new TypeReference<Void>(){}),
                    verticleHelper.getErrorAction(message, UPDATEPETWITHFORM_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, UPDATEPETWITHFORM_SERVICE_ID);
        }
    };
    
     uploadFileHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
            String additionalMetadata = message.body().getString("additionalMetadata");
            File file = new File(message.body().getString("file"));
                service.uploadFile(petId, additionalMetadata, file, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    verticleHelper.getRxResultHandler(message, true, new TypeReference<ModelApiResponse>(){}),
                    verticleHelper.getErrorAction(message, UPLOADFILE_SERVICE_ID)
                );
        } catch (Exception e) {
            verticleHelper.manageError(message, e, UPLOADFILE_SERVICE_ID);
        }
    };
    
    }

    @Override
    public void start() throws Exception {
    vertx.eventBus().<JsonObject> consumer(ADDPET_SERVICE_ID).handler(addPetHandler);
    vertx.eventBus().<JsonObject> consumer(DELETEPET_SERVICE_ID).handler(deletePetHandler);
    vertx.eventBus().<JsonObject> consumer(FINDPETSBYSTATUS_SERVICE_ID).handler(findPetsByStatusHandler);
    vertx.eventBus().<JsonObject> consumer(FINDPETSBYTAGS_SERVICE_ID).handler(findPetsByTagsHandler);
    vertx.eventBus().<JsonObject> consumer(GETPETBYID_SERVICE_ID).handler(getPetByIdHandler);
    vertx.eventBus().<JsonObject> consumer(UPDATEPET_SERVICE_ID).handler(updatePetHandler);
    vertx.eventBus().<JsonObject> consumer(UPDATEPETWITHFORM_SERVICE_ID).handler(updatePetWithFormHandler);
    vertx.eventBus().<JsonObject> consumer(UPLOADFILE_SERVICE_ID).handler(uploadFileHandler);
    
    }

    protected PetApi createServiceImplementation() {
        return new PetApiImpl();
    }
}