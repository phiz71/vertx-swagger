package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

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
    
    //TODO : create Implementation
    PetApi service = new PetApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for addPet
        vertx.eventBus().<JsonObject> consumer(ADDPET_SERVICE_ID).handler(message -> {
            try {
                Pet body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Pet.class);
                
                service.addPet(body);
                message.reply(null);
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+ADDPET_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for deletePet
        vertx.eventBus().<JsonObject> consumer(DELETEPET_SERVICE_ID).handler(message -> {
            try {
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                String apiKey = message.body().getString("api_key");
                
                service.deletePet(petId, apiKey);
                message.reply(null);
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+DELETEPET_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for findPetsByStatus
        vertx.eventBus().<JsonObject> consumer(FINDPETSBYSTATUS_SERVICE_ID).handler(message -> {
            try {
                List<String> status = Json.mapper.readValue(message.body().getJsonArray("status").encode(),
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                
                List<Pet> result = service.findPetsByStatus(status);
                message.reply(new JsonArray(Json.encode(result)).encodePrettily());
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+FINDPETSBYSTATUS_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for findPetsByTags
        vertx.eventBus().<JsonObject> consumer(FINDPETSBYTAGS_SERVICE_ID).handler(message -> {
            try {
                List<String> tags = Json.mapper.readValue(message.body().getJsonArray("tags").encode(),
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                
                List<Pet> result = service.findPetsByTags(tags);
                message.reply(new JsonArray(Json.encode(result)).encodePrettily());
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+FINDPETSBYTAGS_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for getPetById
        vertx.eventBus().<JsonObject> consumer(GETPETBYID_SERVICE_ID).handler(message -> {
            try {
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                
                Pet result = service.getPetById(petId);
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+GETPETBYID_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for updatePet
        vertx.eventBus().<JsonObject> consumer(UPDATEPET_SERVICE_ID).handler(message -> {
            try {
                Pet body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Pet.class);
                
                service.updatePet(body);
                message.reply(null);
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+UPDATEPET_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for updatePetWithForm
        vertx.eventBus().<JsonObject> consumer(UPDATEPETWITHFORM_SERVICE_ID).handler(message -> {
            try {
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                String name = message.body().getString("name");
                String status = message.body().getString("status");
                
                service.updatePetWithForm(petId, name, status);
                message.reply(null);
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+UPDATEPETWITHFORM_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for uploadFile
        vertx.eventBus().<JsonObject> consumer(UPLOADFILE_SERVICE_ID).handler(message -> {
            try {
                Long petId = Json.mapper.readValue(message.body().getString("petId"), Long.class);
                String additionalMetadata = message.body().getString("additionalMetadata");
                File file = Json.mapper.readValue(message.body().getJsonObject("file").encode(), File.class);
                
                ModelApiResponse result = service.uploadFile(petId, additionalMetadata, file);
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
            } catch (PetApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+UPLOADFILE_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
    }
}