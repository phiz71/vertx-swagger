package io.swagger.server.api;

import java.nio.charset.Charset;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.phiz71.vertx.swagger.router.OperationIdServiceIdResolver;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.server.api.verticle.PetApiVerticle;
import io.swagger.server.api.verticle.StoreApiVerticle;
import io.swagger.server.api.verticle.UserApiVerticle;

import io.swagger.server.api.util.SwaggerManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class MainApiVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApiVerticle.class);

    private final MainApiFactory apiFactory;

    public MainApiVerticle(MainApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }
    
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Json.mapper.registerModule(new JavaTimeModule());
    	FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("swagger.json", readFile -> {
            if (readFile.succeeded()) {
                Swagger swagger = new SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")));
                SwaggerManager.getInstance().setSwagger(swagger);
                Router swaggerRouter = SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, vertx.eventBus(), new OperationIdServiceIdResolver());
            
                deployVerticles(startFuture);

                vertx.createHttpServer() 
                    .requestHandler(swaggerRouter::accept) 
                    .listen(config().getInteger("http.port", 8080));
                startFuture.complete();
            } else {
            	startFuture.fail(readFile.cause());
            }
        });        		        
    }
      
    public void deployVerticles(Future<Void> startFuture) {
        
        vertx.deployVerticle(new PetApiVerticle(apiFactory.createPetApi()), res -> {
            if (res.succeeded()) {
                LOGGER.info("PetApiVerticle : Deployed");
            } else {
                startFuture.fail(res.cause());
                LOGGER.error("PetApiVerticle : Deployement failed");
            }
        });
        
        vertx.deployVerticle(new StoreApiVerticle(apiFactory.createStoreApi()), res -> {
            if (res.succeeded()) {
                LOGGER.info("StoreApiVerticle : Deployed");
            } else {
                startFuture.fail(res.cause());
                LOGGER.error("StoreApiVerticle : Deployement failed");
            }
        });
        
        vertx.deployVerticle(new UserApiVerticle(apiFactory.createUserApi()), res -> {
            if (res.succeeded()) {
                LOGGER.info("UserApiVerticle : Deployed");
            } else {
                startFuture.fail(res.cause());
                LOGGER.error("UserApiVerticle : Deployement failed");
            }
        });
        
    }

}