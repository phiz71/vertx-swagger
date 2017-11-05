package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ModelApiResponse extends JsonObject {
  private static final String CODE_KEY = "code";
  private static final String TYPE_KEY = "type";
  private static final String MESSAGE_KEY = "message";

  

  public ModelApiResponse () {
  }

  public ModelApiResponse (JsonObject json) {
    this.put(CODE_KEY, json.getInteger(CODE_KEY));
    this.put(TYPE_KEY, json.getString(TYPE_KEY));
    this.put(MESSAGE_KEY, json.getString(MESSAGE_KEY));
  }

  public ModelApiResponse (Integer code, String type, String message) {

    this.put(CODE_KEY, code);

    this.put(TYPE_KEY, type);

    this.put(MESSAGE_KEY, message);

  }

  @JsonProperty("code")
  public Integer getCode(){
    return this.getInteger(CODE_KEY);
  }

  public ModelApiResponse setCode(Integer code){
    this.put(CODE_KEY, code);
    return this;
  }

  @JsonProperty("type")
  public String getType(){
    return this.getString(TYPE_KEY);
  }

  public ModelApiResponse setType(String type){
    this.put(TYPE_KEY, type);
    return this;
  }

  @JsonProperty("message")
  public String getMessage(){
    return this.getString(MESSAGE_KEY);
  }

  public ModelApiResponse setMessage(String message){
    this.put(MESSAGE_KEY, message);
    return this;
  }

}
