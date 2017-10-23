# Swagger Codegen VertX library

## Overview
This library extends the [Swagger Codegen Generator](https://github.com/swagger-api/swagger-codegen#swagger-code-generator) project in order to generate a java web server based on [Eclipse Vert.X](http://vertx.io) and [Vert.X Web](http://vertx.io/docs/vertx-web/java/) and your swagger definition.

## Getting started 
Here is a [blog post](http://vertx.io/blog/presentation-of-the-vert-x-swagger-project) that explains how to use this library.

### Examples
In the `sample` directory, you may find 2 generated servers based on [Swagger Petstore example](http://petstore.swagger.io/):
* `petstore-vertx-server`
* `petstore-vertx-rx-server`

They are both generated during the test phase of maven build.
The first one, using this command:
```
java -cp /path/to/swagger-codegen-cli-2.2.2.jar:/path/to/vertx-swagger-codegen-1.5.0.jar io.swagger.codegen.SwaggerCodegen generate \
  -l java-vertx \
  -o path/to/destination/folder \
  -i path/to/swagger/definition \
  --group-id your.group.id \
  --artifact-id your.artifact.id
```

The Rx version is generated like before but with a new option "rxInterface":
```bash
java -cp /path/to/swagger-codegen-cli-2.2.2.jar:/path/to/vertx-swagger-codegen-1.5.0.jar io.swagger.codegen.SwaggerCodegen generate \
  -l java-vertx \
  -o path/to/destination/folder \
  -i path/to/swagger/definition \
  --group-id your.group.id \
  --artifact-id your.artifact.id \
  -DrxInterface=true
```

## Servers style
When `-DrxInterface=false` or if `rxInterface` option is not set, the generated API interfaces use Handler<>.
```java
void addPet(Pet body, Handler<AsyncResult<ResourceResponse<Void>>> handler);

void getPetById(Long petId, Handler<AsyncResult<ResourceResponse<Pet>>> handler);
```

When `-DrxInterface=true`, the generated API interfaces use Single<> and Completable.
```java
public Single<ResourceResponse<Void>> addPet(Pet body);

public Single<ResourceResponse<Pet>> getPetById(Long petId);
```

## Generate first implementation
You can use the `-DapiImplGeneration=true` option to create your `XXXApiImpl`. **Be careful since it will override all existing implementation**
