package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class InlineResponseDefault   {
  
  private UUID uuid = null;

  public InlineResponseDefault () {

  }

  public InlineResponseDefault (UUID uuid) {
    this.uuid = uuid;
  }

    
  @JsonProperty("uuid")
  public UUID getUuid() {
    return uuid;
  }
  public InlineResponseDefault setUuid(UUID uuid) {
    this.uuid = uuid;
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
    InlineResponseDefault inlineResponseDefault = (InlineResponseDefault) o;
    return Objects.equals(uuid, inlineResponseDefault.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponseDefault {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
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
