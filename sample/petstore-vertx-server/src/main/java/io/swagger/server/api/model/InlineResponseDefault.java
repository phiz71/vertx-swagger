package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class InlineResponseDefault extends JsonObject {

  private static final String UUID = "uuid";

  public InlineResponseDefault (UUID uuid) {
    this.put(UUID, uuid.toString());
  }

  @JsonProperty("uuid")
  public UUID getUuid() {
    return Json.decodeValue(this.getString(UUID), UUID.class);
  }
  public InlineResponseDefault setUuid(UUID uuid) {
    this.put(UUID, uuid.toString());
    return this;
  }

}
