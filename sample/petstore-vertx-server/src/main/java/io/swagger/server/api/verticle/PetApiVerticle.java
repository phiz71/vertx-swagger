package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
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
                
                
                //TODO: call implementation
                
                service.addPet(body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for deletePet
        vertx.eventBus().<JsonObject> consumer(DELETEPET_SERVICE_ID).handler(message -> {
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
        
        //Consumer for findPetsByStatus
        vertx.eventBus().<JsonObject> consumer(FINDPETSBYSTATUS_SERVICE_ID).handler(message -> {
            try {
                
                List<String> status = Json.mapper.readValue(message.body().getJsonArray("status").encode(), 
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                
                
                //TODO: call implementation
                
                List<Pet> result = service.findPetsByStatus(status);
                message.reply(new JsonArray(Json.encode(result)).encodePrettily());
                
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for findPetsByTags
        vertx.eventBus().<JsonObject> consumer(FINDPETSBYTAGS_SERVICE_ID).handler(message -> {
            try {
                
                List<String> tags = Json.mapper.readValue(message.body().getJsonArray("tags").encode(), 
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                
                
                //TODO: call implementation
                
                List<Pet> result = service.findPetsByTags(tags);
                message.reply(new JsonArray(Json.encode(result)).encodePrettily());
                
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for getPetById
        vertx.eventBus().<JsonObject> consumer(GETPETBYID_SERVICE_ID).handler(message -> {
            try {
                
                Long petId = Json.mapper.readValue(message.body().getJsonObject("petId").encode(), Long.class);
                
                
                //TODO: call implementation
                
                Pet result = service.getPetById(petId);
                
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for updatePet
        vertx.eventBus().<JsonObject> consumer(UPDATEPET_SERVICE_ID).handler(message -> {
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
        
        //Consumer for updatePetWithForm
        vertx.eventBus().<JsonObject> consumer(UPDATEPETWITHFORM_SERVICE_ID).handler(message -> {
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
        
        //Consumer for uploadFile
        vertx.eventBus().<JsonObject> consumer(UPLOADFILE_SERVICE_ID).handler(message -> {
            try {
                
                Long petId = Json.mapper.readValue(message.body().getJsonObject("petId").encode(), Long.class);
                
                String additionalMetadata = Json.mapper.readValue(message.body().getJsonObject("additionalMetadata").encode(), String.class);
                
                File file = Json.mapper.readValue(message.body().getJsonObject("file").encode(), File.class);
                
                
                //TODO: call implementation
                
                ModelApiResponse result = service.uploadFile(petId, additionalMetadata, file);
                
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
    }
}
