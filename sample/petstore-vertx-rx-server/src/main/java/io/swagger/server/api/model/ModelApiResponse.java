package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ModelApiResponse   {
  
  private Integer code = null;
  private String type = null;
  private String message = null;

  public ModelApiResponse () {

  }

  public ModelApiResponse (Integer code, String type, String message) {
    this.code = code;
    this.type = type;
    this.message = message;
  }

    
  @JsonProperty("code")
  public Integer getCode() {
    return code;
  }
  public ModelApiResponse setCode(Integer code) {
    this.code = code;
    return this;
  }

    
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public ModelApiResponse setType(String type) {
    this.type = type;
    return this;
  }

    
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }
  public ModelApiResponse setMessage(String message) {
    this.message = message;
    return this;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelApiResponse _apiResponse = (ModelApiResponse) o;
    return Objects.equals(code, _apiResponse.code) &&
        Objects.equals(type, _apiResponse.type) &&
        Objects.equals(message, _apiResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, type, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelApiResponse {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
