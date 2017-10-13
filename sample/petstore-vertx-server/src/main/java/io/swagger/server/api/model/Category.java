package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

/**
 * some description
 **/
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Category extends JsonObject {
  
  private static final String ID = "id";
  private static final String NAME = "name";

  public Category () {

  }

  public Category (Long id, String name) {
    this.put(ID, id);
    this.put(NAME, name);
  }

    
  @JsonProperty("id")
  public Long getId() {
    return this.getLong(ID);
  }
  public Category setId(Long id) {
    this.put(ID, id);
    return this;
  }

    
  @JsonProperty("name")
  public String getName() {
    return this.getString(NAME);
  }
  public Category setName(String name) {
    this.put(NAME, name);
    return this;
  }

}
