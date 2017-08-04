package io.swagger.server.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class User   {
  
  private Long id = null;
  private String username = null;
  private String firstName = null;
  private String lastName = null;
  private String email = null;
  private String password = null;
  private String phone = null;
  private Integer userStatus = null;

  public User () {

  }

  public User (Long id, String username, String firstName, String lastName, String email, String password, String phone, Integer userStatus) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.userStatus = userStatus;
  }

    
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public User setId(Long id) {
    this.id = id;
    return this;
  }

    
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }
  public User setUsername(String username) {
    this.username = username;
    return this;
  }

    
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }
  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

    
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }
  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

    
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }
  public User setEmail(String email) {
    this.email = email;
    return this;
  }

    
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }
  public User setPassword(String password) {
    this.password = password;
    return this;
  }

    
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }
  public User setPhone(String phone) {
    this.phone = phone;
    return this;
  }

    
  @JsonProperty("userStatus")
  public Integer getUserStatus() {
    return userStatus;
  }
  public User setUserStatus(Integer userStatus) {
    this.userStatus = userStatus;
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
    User user = (User) o;
    return Objects.equals(id, user.id) &&
        Objects.equals(username, user.username) &&
        Objects.equals(firstName, user.firstName) &&
        Objects.equals(lastName, user.lastName) &&
        Objects.equals(email, user.email) &&
        Objects.equals(password, user.password) &&
        Objects.equals(phone, user.phone) &&
        Objects.equals(userStatus, user.userStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstName, lastName, email, password, phone, userStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    userStatus: ").append(toIndentedString(userStatus)).append("\n");
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
