⚠️ DEPRECATED This repository is no longer active ⚠️ 
I should have archived this project a long time ago but it was hard to close my first OSS project on github.

If you were looking for an integration between Eclipse Vert.x and Swagger/OpenAPI, please consider using [Vert.x OpenAPI] https://vertx.io/docs/vertx-web-openapi/java/



# Vert.X - Swagger
This project aims to provide a code generator based on Swagger to create web server with Eclipse Vert.X and Vert.X Web.
It contains 2 modules:
 - **vertx-swagger-router** : a generic Router which can be configured with a swagger definition
 - **vertx-swagger-codegen** : a library which has to be used with [Swagger Codegen Generator](https://github.com/swagger-api/swagger-codegen#swagger-code-generator)
  
# Getting started 
Here is a [blog post](http://vertx.io/blog/presentation-of-the-vert-x-swagger-project) that explains how to use these libraries.
  
# Maven :
## vertx-swagger-parent :
```XML
<dependency>
  <groupId>com.github.phiz71</groupId>
  <artifactId>vertx-swagger-parent</artifactId>
  <version>1.6.0</version>
  <type>pom</type>
</dependency>
```

## vertx-swagger-router :
```XML
<dependency>
  <groupId>com.github.phiz71</groupId>
  <artifactId>vertx-swagger-router</artifactId>
  <version>1.6.0</version>
</dependency>
```

## vertx-swagger-codegen :
```XML
<dependency>
  <groupId>com.github.phiz71</groupId>
  <artifactId>vertx-swagger-codegen</artifactId>
  <version>1.6.0</version>
</dependency>
```
