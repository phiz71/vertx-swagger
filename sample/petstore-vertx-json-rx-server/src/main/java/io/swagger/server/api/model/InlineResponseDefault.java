package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class InlineResponseDefault extends JsonObject {
  private static final String UUID_KEY = "uuid";

  

  public InlineResponseDefault () {
  }

  public InlineResponseDefault (JsonObject json) {
    this.put(UUID_KEY, json.getString(UUID_KEY));
  }

  public InlineResponseDefault (String uuid) {

    this.put(UUID_KEY, uuid);

  }

  @JsonProperty("uuid")
  public String getUuid(){
    return this.getString(UUID_KEY);
  }

  public InlineResponseDefault setUuid(String uuid){
    this.put(UUID_KEY, uuid);
    return this;
  }

}
