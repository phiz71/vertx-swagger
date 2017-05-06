[![Build Status](https://travis-ci.org/phiz71/vertx-swagger.svg?branch=master)](https://travis-ci.org/phiz71/vertx-swagger)
# Vert.X - Swagger
This project aims to provide a code generator based on Swagger to create web server with Vert.X and Vert.X Web.
It contains 2 modules:
 - **vertx-swagger-router** : a generic Router which can be configured with a swagger definition
 - **vertx-swagger-codegen** : a library which has to be used with [Swagger Codegen Generator](https://github.com/swagger-api/swagger-codegen#swagger-code-generator)
  
 
# Todo :
## vertx-swagger-router :
 - manage the authorization part of swagger definition
 - use more data from swagger definition if possible (responses, info)
  
## vertx-swagger-codegen :
 - add the possibility to validate the request according the swagger definition
 - support other languages (Groovy, JavaScript, ruby, Ceylon)
  
# Maven :
## vertx-swagger-parent :
```xml
    <dependency>
        <groupId>com.github.phiz71</groupId>
        <artifactId>vertx-swagger-parent</artifactId>
        <version>1.0.0</version>
        <type>pom</type>
    </dependency>
```

## vertx-swagger-router :
```xml
    <dependency>
        <groupId>com.github.phiz71</groupId>
        <artifactId>vertx-swagger-router</artifactId>
        <version>1.0.0</version>
    </dependency>
```

## vertx-swagger-codegen :
```xml
    <dependency>
        <groupId>com.github.phiz71</groupId>
        <artifactId>vertx-swagger-codegen</artifactId>
        <version>1.0.0</version>
    </dependency>
```
