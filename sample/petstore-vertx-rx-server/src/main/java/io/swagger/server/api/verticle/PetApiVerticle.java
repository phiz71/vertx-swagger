package io.swagger.server.api.verticle;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.User;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import java.io.File;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.ModelApiResponse;
import io.swagger.server.api.model.Pet;

import java.util.List;
import java.util.Map;

public class PetApiVerticle extends AbstractVerticle {
    final static Logger LOGGER = LoggerFactory.getLogger(PetApiVerticle.class); 
    
    final static String ADDPET_SERVICE_ID = "addPet";
    final static String DELETEPET_SERVICE_ID = "deletePet";
    final static String FINDPETSBYSTATUS_SERVICE_ID = "findPetsByStatus";
    final static String FINDPETSBYTAGS_SERVICE_ID = "findPetsByTags";
    final static String GETPETBYID_SERVICE_ID = "getPetById";
    final static String UPDATEPET_SERVICE_ID = "updatePet";
    final static String UPDATEPETWITHFORM_SERVICE_ID = "updatePetWithForm";
    final static String UPLOADFILE_SERVICE_ID = "uploadFile";
    

    protected PetApi service = createServiceImplementation();

    @Override
    public void start() throws Exception {
        
        //Consumer for addPet
        vertx.eventBus().<JsonObject> consumer(ADDPET_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                Pet body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Pet.class);
                service.addPet(body, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, ADDPET_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, ADDPET_SERVICE_ID);
            }
        });
        
        //Consumer for deletePet
        vertx.eventBus().<JsonObject> consumer(DELETEPET_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                String apiKey = message.body().getString("api_key");
                service.deletePet(petId, apiKey, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, DELETEPET_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, DELETEPET_SERVICE_ID);
            }
        });
        
        //Consumer for findPetsByStatus
        vertx.eventBus().<JsonObject> consumer(FINDPETSBYSTATUS_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                List<String> status = Json.mapper.readValue(message.body().getJsonArray("status").encode(), new TypeReference<List<String>>(){});
                service.findPetsByStatus(status, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    result -> {
                        message.reply(new JsonArray(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, FINDPETSBYSTATUS_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, FINDPETSBYSTATUS_SERVICE_ID);
            }
        });
        
        //Consumer for findPetsByTags
        vertx.eventBus().<JsonObject> consumer(FINDPETSBYTAGS_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                List<String> tags = Json.mapper.readValue(message.body().getJsonArray("tags").encode(), new TypeReference<List<String>>(){});
                service.findPetsByTags(tags, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    result -> {
                        message.reply(new JsonArray(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, FINDPETSBYTAGS_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, FINDPETSBYTAGS_SERVICE_ID);
            }
        });
        
        //Consumer for getPetById
        vertx.eventBus().<JsonObject> consumer(GETPETBYID_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                service.getPetById(petId, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    result -> {
                        message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, GETPETBYID_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, GETPETBYID_SERVICE_ID);
            }
        });
        
        //Consumer for updatePet
        vertx.eventBus().<JsonObject> consumer(UPDATEPET_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                Pet body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Pet.class);
                service.updatePet(body, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, UPDATEPET_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, UPDATEPET_SERVICE_ID);
            }
        });
        
        //Consumer for updatePetWithForm
        vertx.eventBus().<JsonObject> consumer(UPDATEPETWITHFORM_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                String name = message.body().getString("name");
                String status = message.body().getString("status");
                service.updatePetWithForm(petId, name, status, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, UPDATEPETWITHFORM_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, UPDATEPETWITHFORM_SERVICE_ID);
            }
        });
        
        //Consumer for uploadFile
        vertx.eventBus().<JsonObject> consumer(UPLOADFILE_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                String additionalMetadata = message.body().getString("additionalMetadata");
                File file = Json.mapper.readValue(message.body().getJsonObject("file").encode(), File.class);
                service.uploadFile(petId, additionalMetadata, file, io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    result -> {
                        message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, UPLOADFILE_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, UPLOADFILE_SERVICE_ID);
            }
        });
        
    }
    
    private void manageError(Message<JsonObject> message, Throwable cause, String serviceName) {
        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
        if (cause instanceof MainApiException) {
            code = ((MainApiException)cause).getStatusCode();
            statusMessage = ((MainApiException)cause).getStatusMessage();
        } else {
            logUnexpectedError(serviceName, cause); 
        }
            
        message.fail(code, statusMessage);
    }
    
    private void logUnexpectedError(String serviceName, Throwable cause) {
        LOGGER.error("Unexpected error in "+ serviceName, cause);
    }

    protected PetApi createServiceImplementation() {
        return new PetApiImpl();
    }
}
