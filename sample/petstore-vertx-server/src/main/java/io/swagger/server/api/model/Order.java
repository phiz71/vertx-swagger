package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Order   {
  
  private Long id = null;
  private Long petId = null;
  private Integer quantity = null;
  private OffsetDateTime shipDate = null;


  public enum StatusEnum {
    PLACED("placed"),
    APPROVED("approved"),
    DELIVERED("delivered");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return value;
    }
  }

  private StatusEnum status = null;
  private Boolean complete = false;

  public Order () {

  }

  public Order (Long id, Long petId, Integer quantity, OffsetDateTime shipDate, StatusEnum status, Boolean complete) {
    this.id = id;
    this.petId = petId;
    this.quantity = quantity;
    this.shipDate = shipDate;
    this.status = status;
    this.complete = complete;
  }

    
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public Order setId(Long id) {
    this.id = id;
    return this;
  }

    
  @JsonProperty("petId")
  public Long getPetId() {
    return petId;
  }
  public Order setPetId(Long petId) {
    this.petId = petId;
    return this;
  }

    
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }
  public Order setQuantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

    
  @JsonProperty("shipDate")
  public OffsetDateTime getShipDate() {
    return shipDate;
  }
  public Order setShipDate(OffsetDateTime shipDate) {
    this.shipDate = shipDate;
    return this;
  }

    
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }
  public Order setStatus(StatusEnum status) {
    this.status = status;
    return this;
  }

    
  @JsonProperty("complete")
  public Boolean getComplete() {
    return complete;
  }
  public Order setComplete(Boolean complete) {
    this.complete = complete;
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
    Order order = (Order) o;
    return Objects.equals(id, order.id) &&
        Objects.equals(petId, order.petId) &&
        Objects.equals(quantity, order.quantity) &&
        Objects.equals(shipDate, order.shipDate) &&
        Objects.equals(status, order.status) &&
        Objects.equals(complete, order.complete);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, petId, quantity, shipDate, status, complete);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Order {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    petId: ").append(toIndentedString(petId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    shipDate: ").append(toIndentedString(shipDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
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
