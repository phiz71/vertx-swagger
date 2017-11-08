package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Order extends JsonObject {
  private static final String ID_KEY = "id";
  private static final String PET_ID_KEY = "petId";
  private static final String QUANTITY_KEY = "quantity";
  private static final String SHIP_DATE_KEY = "shipDate";
  private static final String STATUS_KEY = "status";
  private static final String COMPLETE_KEY = "complete";

  
  public final class OrderStatus {
    private OrderStatus(){}
    public static final String PLACED = "placed";
    public static final String APPROVED = "approved";
    public static final String DELIVERED = "delivered";
    
  }


  public Order () {
  }

  public Order (JsonObject json) {
    this.put(ID_KEY, json.getLong(ID_KEY));
    this.put(PET_ID_KEY, json.getLong(PET_ID_KEY));
    this.put(QUANTITY_KEY, json.getInteger(QUANTITY_KEY));
    this.put(SHIP_DATE_KEY, json.getInstant(SHIP_DATE_KEY));
    this.put(STATUS_KEY, json.getString(STATUS_KEY));
    this.put(COMPLETE_KEY, json.getBoolean(COMPLETE_KEY));
  }

  public Order (Long id, Long petId, Integer quantity, Instant shipDate, String status, Boolean complete) {

    this.put(ID_KEY, id);

    this.put(PET_ID_KEY, petId);

    this.put(QUANTITY_KEY, quantity);

    this.put(SHIP_DATE_KEY, shipDate);

    this.put(STATUS_KEY, status);

    this.put(COMPLETE_KEY, complete);

  }

  @JsonProperty("id")
  public Long getId(){
    return this.getLong(ID_KEY);
  }

  public Order setId(Long id){
    this.put(ID_KEY, id);
    return this;
  }

  @JsonProperty("petId")
  public Long getPetId(){
    return this.getLong(PET_ID_KEY);
  }

  public Order setPetId(Long petId){
    this.put(PET_ID_KEY, petId);
    return this;
  }

  @JsonProperty("quantity")
  public Integer getQuantity(){
    return this.getInteger(QUANTITY_KEY);
  }

  public Order setQuantity(Integer quantity){
    this.put(QUANTITY_KEY, quantity);
    return this;
  }

  @JsonProperty("shipDate")
  public Instant getShipDate(){
    return this.getInstant(SHIP_DATE_KEY);
  }

  public Order setShipDate(Instant shipDate){
    this.put(SHIP_DATE_KEY, shipDate);
    return this;
  }

  @JsonProperty("status")
  public String getStatus(){
    return this.getString(STATUS_KEY);
  }

  public Order setStatus(String status){
    this.put(STATUS_KEY, status);
    return this;
  }

  @JsonProperty("complete")
  public Boolean getComplete(){
    return this.getBoolean(COMPLETE_KEY);
  }

  public Order setComplete(Boolean complete){
    this.put(COMPLETE_KEY, complete);
    return this;
  }

}
