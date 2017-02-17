[![Build Status](https://travis-ci.org/phiz71/vertx-swagger.svg?branch=master)](https://travis-ci.org/phiz71/vertx-swagger)
# Vert.X - Swagger
This project aims to provide a code generator based on Swagger to create web server with Vert.X and Vert.X Web.
It contains 2 modules:
 - **vertx-swagger-router** : a generic Router which can be configured with a swagger definition
 - **vertx-swagger-codegen** : a library which has to be used with [Swagger Codegen Generator](https://github.com/swagger-api/swagger-codegen#swagger-code-generator)
  
 
# todo :
## vertx-swagger-router :
 - manage the authorization part of swagger definition
 - use more data from swagger definition if possible (responses, info)
  
## vertx-swagger-codegen :
 - add the possibility to validate the request according the swagger definition
 - support other languages (Groovy, JavaScript, ruby, Ceylon)
 - generate swagger definition using swagger annotations.
  
