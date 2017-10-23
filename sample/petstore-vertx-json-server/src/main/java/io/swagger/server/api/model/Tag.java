package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Tag extends JsonObject {
  private static final String ID_KEY = "id";
  private static final String NAME_KEY = "name";

  

  public Tag () {
  }

  public Tag (JsonObject json) {
    this.put(ID_KEY, json.getLong(ID_KEY));
    this.put(NAME_KEY, json.getString(NAME_KEY));
  }

  public Tag (Long id, String name) {

    this.put(ID_KEY, id);

    this.put(NAME_KEY, name);

  }

  @JsonProperty("id")
  public Long getId(){
    return this.getLong(ID_KEY);
  }

  public Tag setId(Long id){
    this.put(ID_KEY, id);
    return this;
  }

  @JsonProperty("name")
  public String getName(){
    return this.getString(NAME_KEY);
  }

  public Tag setName(String name){
    this.put(NAME_KEY, name);
    return this;
  }

}
