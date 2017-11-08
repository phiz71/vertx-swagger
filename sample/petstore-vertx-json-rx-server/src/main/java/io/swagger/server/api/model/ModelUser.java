package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ModelUser extends JsonObject {
  private static final String ID_KEY = "id";
  private static final String USERNAME_KEY = "username";
  private static final String FIRST_NAME_KEY = "firstName";
  private static final String LAST_NAME_KEY = "lastName";
  private static final String EMAIL_KEY = "email";
  private static final String PASSWORD_KEY = "password";
  private static final String PHONE_KEY = "phone";
  private static final String USER_STATUS_KEY = "userStatus";

  

  public ModelUser () {
  }

  public ModelUser (JsonObject json) {
    this.put(ID_KEY, json.getLong(ID_KEY));
    this.put(USERNAME_KEY, json.getString(USERNAME_KEY));
    this.put(FIRST_NAME_KEY, json.getString(FIRST_NAME_KEY));
    this.put(LAST_NAME_KEY, json.getString(LAST_NAME_KEY));
    this.put(EMAIL_KEY, json.getString(EMAIL_KEY));
    this.put(PASSWORD_KEY, json.getString(PASSWORD_KEY));
    this.put(PHONE_KEY, json.getString(PHONE_KEY));
    this.put(USER_STATUS_KEY, json.getInteger(USER_STATUS_KEY));
  }

  public ModelUser (Long id, String username, String firstName, String lastName, String email, String password, String phone, Integer userStatus) {

    this.put(ID_KEY, id);

    this.put(USERNAME_KEY, username);

    this.put(FIRST_NAME_KEY, firstName);

    this.put(LAST_NAME_KEY, lastName);

    this.put(EMAIL_KEY, email);

    this.put(PASSWORD_KEY, password);

    this.put(PHONE_KEY, phone);

    this.put(USER_STATUS_KEY, userStatus);

  }

  @JsonProperty("id")
  public Long getId(){
    return this.getLong(ID_KEY);
  }

  public ModelUser setId(Long id){
    this.put(ID_KEY, id);
    return this;
  }

  @JsonProperty("username")
  public String getUsername(){
    return this.getString(USERNAME_KEY);
  }

  public ModelUser setUsername(String username){
    this.put(USERNAME_KEY, username);
    return this;
  }

  @JsonProperty("firstName")
  public String getFirstName(){
    return this.getString(FIRST_NAME_KEY);
  }

  public ModelUser setFirstName(String firstName){
    this.put(FIRST_NAME_KEY, firstName);
    return this;
  }

  @JsonProperty("lastName")
  public String getLastName(){
    return this.getString(LAST_NAME_KEY);
  }

  public ModelUser setLastName(String lastName){
    this.put(LAST_NAME_KEY, lastName);
    return this;
  }

  @JsonProperty("email")
  public String getEmail(){
    return this.getString(EMAIL_KEY);
  }

  public ModelUser setEmail(String email){
    this.put(EMAIL_KEY, email);
    return this;
  }

  @JsonProperty("password")
  public String getPassword(){
    return this.getString(PASSWORD_KEY);
  }

  public ModelUser setPassword(String password){
    this.put(PASSWORD_KEY, password);
    return this;
  }

  @JsonProperty("phone")
  public String getPhone(){
    return this.getString(PHONE_KEY);
  }

  public ModelUser setPhone(String phone){
    this.put(PHONE_KEY, phone);
    return this;
  }

  @JsonProperty("userStatus")
  public Integer getUserStatus(){
    return this.getInteger(USER_STATUS_KEY);
  }

  public ModelUser setUserStatus(Integer userStatus){
    this.put(USER_STATUS_KEY, userStatus);
    return this;
  }

}
