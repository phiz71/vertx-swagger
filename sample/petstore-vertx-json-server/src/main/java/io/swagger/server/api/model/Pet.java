package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.Tag;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Pet extends JsonObject {
  private static final String ID_KEY = "id";
  private static final String CATEGORY_KEY = "category";
  private static final String NAME_KEY = "name";
  private static final String PHOTO_URLS_KEY = "photoUrls";
  private static final String TAGS_KEY = "tags";
  private static final String STATUS_KEY = "status";

  
  public final class PetStatus {
    private PetStatus(){}
    public static final String AVAILABLE = "available";
    public static final String PENDING = "pending";
    public static final String SOLD = "sold";
    
  }


  public Pet () {
  }

  public Pet (JsonObject json) {
    this.put(ID_KEY, json.getLong(ID_KEY));
    this.put(CATEGORY_KEY, json.getJsonObject(CATEGORY_KEY));
    this.put(NAME_KEY, json.getString(NAME_KEY));
    this.put(PHOTO_URLS_KEY, json.getJsonArray(PHOTO_URLS_KEY));
    this.put(TAGS_KEY, json.getJsonArray(TAGS_KEY));
    this.put(STATUS_KEY, json.getString(STATUS_KEY));
  }

  public Pet (Long id, Category category, String name, JsonArray photoUrls, JsonArray tags, String status) {

    this.put(ID_KEY, id);

    this.put(CATEGORY_KEY, category);

    this.put(NAME_KEY, name);

    this.put(PHOTO_URLS_KEY, photoUrls);

    this.put(TAGS_KEY, tags);

    this.put(STATUS_KEY, status);

  }

  @JsonProperty("id")
  public Long getId(){
    return this.getLong(ID_KEY);
  }

  public Pet setId(Long id){
    this.put(ID_KEY, id);
    return this;
  }

  @JsonProperty("category")
  public Category getCategory(){
    return (Category) this.getJsonObject(CATEGORY_KEY);
  }

  public Pet setCategory(Category category){
    this.put(CATEGORY_KEY, category);
    return this;
  }

  @JsonProperty("name")
  public String getName(){
    return this.getString(NAME_KEY);
  }

  public Pet setName(String name){
    this.put(NAME_KEY, name);
    return this;
  }

  @JsonProperty("photoUrls")
  public JsonArray getPhotoUrls(){
    return this.getJsonArray(PHOTO_URLS_KEY);
  }

  public Pet setPhotoUrls(JsonArray photoUrls){
    this.put(PHOTO_URLS_KEY, photoUrls);
    return this;
  }

  @JsonProperty("tags")
  public JsonArray getTags(){
    return this.getJsonArray(TAGS_KEY);
  }

  public Pet setTags(JsonArray tags){
    this.put(TAGS_KEY, tags);
    return this;
  }

  @JsonProperty("status")
  public String getStatus(){
    return this.getString(STATUS_KEY);
  }

  public Pet setStatus(String status){
    this.put(STATUS_KEY, status);
    return this;
  }

}
