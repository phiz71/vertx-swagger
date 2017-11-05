package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

/**
 * some description
 **/
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Category extends JsonObject {
  private static final String ID_KEY = "id";
  private static final String NAME_KEY = "name";

  

  public Category () {
  }

  public Category (JsonObject json) {
    this.put(ID_KEY, json.getLong(ID_KEY));
    this.put(NAME_KEY, json.getString(NAME_KEY));
  }

  public Category (Long id, String name) {

    this.put(ID_KEY, id);

    this.put(NAME_KEY, name);

  }

  @JsonProperty("id")
  public Long getId(){
    return this.getLong(ID_KEY);
  }

  public Category setId(Long id){
    this.put(ID_KEY, id);
    return this;
  }

  @JsonProperty("name")
  public String getName(){
    return this.getString(NAME_KEY);
  }

  public Category setName(String name){
    this.put(NAME_KEY, name);
    return this;
  }

}
