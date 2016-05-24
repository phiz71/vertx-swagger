# Swagger Codegen VertX library

## Overview
This library extends the [Swagger Codegen Generator](https://github.com/swagger-api/swagger-codegen#swagger-code-generator) project in order to generate a java web server based on [Vert.X](http://vertx.io) and [Vert.X Web](http://vertx.io/docs/vertx-web/java/) and your swagger definition.

## How do I use this?
Once you've clone the repository, you can run :

```
mvn package
```

In your generator project, a single jar file will be produced `target/vertx-swagger-codegen-1.0.0-SNAPSHOT.jar`.  You can now use that with codegen:

```
java -cp /path/to/swagger-codegen-distribution:/path/to/vertx-swagger-codegen-1.0.0-SNAPSHOT.jar io.swagger.codegen.SwaggerCodegen -l java-vertx -o ./test -i path/toSwagger/definition --group-id your.group.id --artifact-id my.api.vertx
```

Congratulations, you've just created a maven project...


## Wait... but... it does not compile
You're right, but don't worry.
Unfortunately, this generator won't do all the job, and you'll have to implement some interfaces.
As you could see, for each "Tag" in a swagger definition, the library generates a Vert.X Verticle and an Java Interface which contains a method per route


###Example
Have a look into `../sample` : a petstore-vertx-server based on [Swagger Petstore example](http://petstore.swagger.io/) is generated during the test phase of maven build.