# Eclipse Vert.x/Web Swagger Router

This project aims at having a generic Vert.X Router which is configure dynamically with a swagger defitinion (json or yaml format).
It's using [Swagger-Parser](https://github.com/swagger-api/swagger-parser#usage)

Here is a [blog post](http://vertx.io/blog/presentation-of-the-vert-x-swagger-project) that explains how to use this library.

## Usage:

```java
    //using Swagger-Parser to parse Swagger Definition to a Swagger Object from URL
    Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
    //...or from local file
    Swagger swagger = new SwaggerParser().read("./path/to/swagger.json");
    
    //using this Swagger object to create a Vert.X Router
    Router swaggerRouter = SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, vertx.eventBus());
```

Paths, query, header, form and body parameters are supported and can be parsed.

### Example:
With this Json

    {
      "swagger" : "2.0",
      "info" : {
        "version" : "1.0.0",
        "title" : "Swagger Test"
      },
      "paths" : {
        "/hello" : {
          "get" : {
            "operationId" : "test.dummy"
          }
        }
      }
    }

The SwaggerRouter will be automatically configured as if you've done this
```java
    router.get("/hello").handler(req -> {
        vertx.eventBus().send("GET_hello", null, result -> {
            if (result.succeeded()) {
                req.response().write(result.toString());
            } else {
                req.fail(result.cause());
            }
        });
    });
```

