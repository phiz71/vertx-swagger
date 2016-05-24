package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.ModelApiResponse;
import java.io.File;

import java.util.List;
import java.util.Map;

public class PetApiVerticle extends AbstractVerticle {
    final static Logger LOGGER = LoggerFactory.getLogger(PetApiVerticle.class); 
    
    final static String POST_PET_SERVICE_ID = "POST_pet";
    final static String DELETE_PET_PETID_SERVICE_ID = "DELETE_pet_petId";
    final static String GET_PET_FINDBYSTATUS_SERVICE_ID = "GET_pet_findByStatus";
    final static String GET_PET_FINDBYTAGS_SERVICE_ID = "GET_pet_findByTags";
    final static String GET_PET_PETID_SERVICE_ID = "GET_pet_petId";
    final static String PUT_PET_SERVICE_ID = "PUT_pet";
    final static String POST_PET_PETID_SERVICE_ID = "POST_pet_petId";
    final static String POST_PET_PETID_UPLOADIMAGE_SERVICE_ID = "POST_pet_petId_uploadImage";
    
    
    //TODO : create Implementation
    PetApi service = new PetApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for POST_pet
        vertx.eventBus().<JsonObject> consumer(POST_PET_SERVICE_ID).handler(message -> {
            try {
                
                Pet body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Pet.class);
                
                
                //TODO: call implementation
                service.addPet(body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for DELETE_pet_petId
        vertx.eventBus().<JsonObject> consumer(DELETE_PET_PETID_SERVICE_ID).handler(message -> {
            try {
                
                Long petId = Json.mapper.readValue(message.body().getJsonObject("petId").encode(), Long.class);
                
                String apiKey = Json.mapper.readValue(message.body().getJsonObject("apiKey").encode(), String.class);
                
                
                //TODO: call implementation
                service.deletePet(petId, apiKey);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_pet_findByStatus
        vertx.eventBus().<JsonObject> consumer(GET_PET_FINDBYSTATUS_SERVICE_ID).handler(message -> {
            try {
                
                List<String> status = Json.mapper.readValue(message.body().getJsonArray("status").encode(), 
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                
                
                //TODO: call implementation
                List<Pet> result = service.findPetsByStatus(status);
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_pet_findByTags
        vertx.eventBus().<JsonObject> consumer(GET_PET_FINDBYTAGS_SERVICE_ID).handler(message -> {
            try {
                
                List<String> tags = Json.mapper.readValue(message.body().getJsonArray("tags").encode(), 
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                
                
                //TODO: call implementation
                List<Pet> result = service.findPetsByTags(tags);
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_pet_petId
        vertx.eventBus().<JsonObject> consumer(GET_PET_PETID_SERVICE_ID).handler(message -> {
            try {
                
                Long petId = Json.mapper.readValue(message.body().getJsonObject("petId").encode(), Long.class);
                
                
                //TODO: call implementation
                Pet result = service.getPetById(petId);
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for PUT_pet
        vertx.eventBus().<JsonObject> consumer(PUT_PET_SERVICE_ID).handler(message -> {
            try {
                
                Pet body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Pet.class);
                
                
                //TODO: call implementation
                service.updatePet(body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for POST_pet_petId
        vertx.eventBus().<JsonObject> consumer(POST_PET_PETID_SERVICE_ID).handler(message -> {
            try {
                
                Long petId = Json.mapper.readValue(message.body().getJsonObject("petId").encode(), Long.class);
                
                String name = Json.mapper.readValue(message.body().getJsonObject("name").encode(), String.class);
                
                String status = Json.mapper.readValue(message.body().getJsonObject("status").encode(), String.class);
                
                
                //TODO: call implementation
                service.updatePetWithForm(petId, name, status);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for POST_pet_petId_uploadImage
        vertx.eventBus().<JsonObject> consumer(POST_PET_PETID_UPLOADIMAGE_SERVICE_ID).handler(message -> {
            try {
                
                Long petId = Json.mapper.readValue(message.body().getJsonObject("petId").encode(), Long.class);
                
                String additionalMetadata = Json.mapper.readValue(message.body().getJsonObject("additionalMetadata").encode(), String.class);
                
                File file = Json.mapper.readValue(message.body().getJsonObject("file").encode(), File.class);
                
                
                //TODO: call implementation
                ModelApiResponse result = service.uploadFile(petId, additionalMetadata, file);
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
    }
}
