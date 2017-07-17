package com.github.phiz71.vertx.swagger.codegen;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

public class JavaVertXServerGenerator extends AbstractJavaCodegen {

    protected String resourceFolder = "src/main/resources";
    protected String apiVersion = "1.0.0-SNAPSHOT";

    public static final String ROOT_PACKAGE = "io.swagger.server.api";
    public static final String VERTX_SWAGGER_ROUTER_VERSION = "vertxSwaggerRouterVersion";
    public static final String RX_INTERFACE_OPTION = "rxInterface";
    public static final String MAIN_API_VERTICAL_GENERATION_OPTION = "mainVerticleGeneration";

    public JavaVertXServerGenerator() {
        super();

        
        // set the output folder here
        outputFolder = "generated-code/javaVertXServer";

        /*
         * Models. You can write model files using the modelTemplateFiles map.
         * if you want to create one template for file, you can do so here. for
         * multiple files for model, just put another entry in the
         * `modelTemplateFiles` with a different extension
         */

        modelTemplateFiles.clear();
        modelTemplateFiles.put("model.mustache", // the template to use
                ".java"); // the extension for each file to write

        /*
         * Api classes. You can write classes for each Api file with the
         * apiTemplateFiles map. as with models, add multiple entries with
         * different extensions for multiple files per class
         */
        apiTemplateFiles.clear();
        apiTemplateFiles.put("api.mustache", // the template to use
                ".java"); // the extension for each file to write
        apiTemplateFiles.put("apiVerticle.mustache", // the template to use
                "Verticle.java"); // the extension for each file to write
        apiTemplateFiles.put("apiException.mustache", // the template to use
                "Exception.java"); // the extension for each file to write

        /*
         * Template Location. This is the location which templates will be read
         * from. The generator will use the resource stream to attempt to read
         * the templates.
         */
        embeddedTemplateDir = templateDir = "javaVertXServer";

        /*
         * Api Package. Optional, if needed, this can be used in templates
         */
        apiPackage = ROOT_PACKAGE + ".verticle";

        /*
         * Model Package. Optional, if needed, this can be used in templates
         */
        modelPackage = ROOT_PACKAGE + ".model";

        /*
         * Invoker Package. Optional, if needed, this can be used in templates
         */
        invokerPackage = ROOT_PACKAGE;

        groupId = "io.swagger";
        artifactId = "swagger-java-vertx-server";
        artifactVersion = apiVersion;
        this.setDateLibrary("java8");

        String vertxSwaggerRouterVersion = ResourceBundle.getBundle("vertx-swagger-router").getString("vertx-swagger-router.version");
        additionalProperties.put(VERTX_SWAGGER_ROUTER_VERSION, vertxSwaggerRouterVersion);
        
        cliOptions.add(CliOption.newBoolean(RX_INTERFACE_OPTION,
                "When specified, API interfaces are generated with RX and methods return Single<>."));

        cliOptions.add(CliOption.newBoolean(MAIN_API_VERTICAL_GENERATION_OPTION,
                "When specified, MainApiVerticle.java will not be generated"));
    }

    /**
     * Configures the type of generator.
     *
     * @return the CodegenType for this generator
     * @see io.swagger.codegen.CodegenType
     */
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    /**
     * Configures a friendly name for the generator. This will be used by the
     * generator to select the library with the -l flag.
     *
     * @return the friendly name for the generator
     */
    public String getName() {
        return "java-vertx";
    }

    /**
     * Returns human-friendly help for the generator. Provide the consumer with
     * help tips, parameters here
     *
     * @return A string value for the help message
     */
    public String getHelp() {
        return "Generates a java-Vert.X Server library.";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        apiTestTemplateFiles.clear();

        importMapping.remove("JsonCreator");
        importMapping.remove("com.fasterxml.jackson.annotation.JsonProperty");
        importMapping.put("JsonInclude", "com.fasterxml.jackson.annotation.JsonInclude");
        importMapping.put("JsonProperty", "com.fasterxml.jackson.annotation.JsonProperty");
        importMapping.put("JsonValue", "com.fasterxml.jackson.annotation.JsonValue");
        importMapping.put("MainApiException", invokerPackage+".MainApiException");

        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();

        supportingFiles.clear();
        supportingFiles.add(new SupportingFile("swagger.mustache", resourceFolder, "swagger.json"));

        if(Boolean.parseBoolean(additionalProperties.getOrDefault(MAIN_API_VERTICAL_GENERATION_OPTION, "true").toString())) {
            supportingFiles.add(new SupportingFile("MainApiVerticle.mustache", sourceFolder + File.separator + invokerPackage.replace(".", File.separator), "MainApiVerticle.java"));
        }
        supportingFiles.add(new SupportingFile("MainApiException.mustache", sourceFolder + File.separator + invokerPackage.replace(".", File.separator), "MainApiException.java"));

        writeOptional(outputFolder, new SupportingFile("vertx-default-jul-logging.mustache", resourceFolder, "vertx-default-jul-logging.properties"));
        writeOptional(outputFolder, new SupportingFile("pom.mustache", "", "pom.xml"));
        writeOptional(outputFolder, new SupportingFile("README.mustache", "", "README.md"));
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if (!model.isEnum) {
            model.imports.add("JsonInclude");
            model.imports.add("JsonProperty");
            if (model.hasEnums) {
                model.imports.add("JsonValue");
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        Map<String, Object> newObjs = super.postProcessOperations(objs);
        Map<String, Object> operations = (Map<String, Object>) newObjs.get("operations");
        if (operations != null) {
            List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
            for (CodegenOperation operation : ops) {
                operation.httpMethod = operation.httpMethod.toLowerCase();

                if ("Void".equalsIgnoreCase(operation.returnType)) {
                    operation.returnType = null;
                }

                if (operation.getHasPathParams()) {
                    operation.path = camelizePath(operation.path);
                }
                
            }
        }
        return newObjs;
    }

    
    
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        CodegenOperation codegenOperation =  super.fromOperation(path, httpMethod, operation, definitions, swagger);
        codegenOperation.imports.add("MainApiException");
        return codegenOperation;
    }
    

    @Override
    public CodegenModel fromModel(String name, Model model, Map<String, Model> allDefinitions) {
        CodegenModel codegenModel = super.fromModel(name, model, allDefinitions);
        codegenModel.imports.remove("ApiModel");
        codegenModel.imports.remove("ApiModelProperty");
        return codegenModel;

    }

    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);

        // add full swagger definition in a mustache parameter
        String swaggerDef = Json.pretty(swagger);
        this.additionalProperties.put("fullSwagger", swaggerDef);

        // add server port from the swagger file, 8080 by default
        String host = swagger.getHost();
        String port = extractPortFromHost(host);
        this.additionalProperties.put("serverPort", port);

        // retrieve api version from swagger file, 1.0.0-SNAPSHOT by default
        if (swagger.getInfo() != null && swagger.getInfo().getVersion() != null)
            artifactVersion = apiVersion = swagger.getInfo().getVersion();
        else
            artifactVersion = apiVersion;

        // manage operation & custom serviceId
        Map<String, Path> paths = swagger.getPaths();
        if (paths != null) {
            for (Entry<String, Path> entry : paths.entrySet()) {
                manageOperationNames(entry.getValue(), entry.getKey());
            }
        }
        this.additionalProperties.remove("gson");
    }

    private void manageOperationNames(Path path, String pathname) {
        String serviceIdTemp;

        Map<HttpMethod, Operation> operationMap = path.getOperationMap();
        if (operationMap != null) {
            for (Entry<HttpMethod, Operation> entry : operationMap.entrySet()) {
                serviceIdTemp = computeServiceId(pathname, entry);
                entry.getValue().setVendorExtension("x-serviceId", serviceIdTemp);
                entry.getValue().setVendorExtension("x-serviceId-varName", serviceIdTemp.toUpperCase() + "_SERVICE_ID");
            }
        }
    }

    private String computeServiceId(String pathname, Entry<HttpMethod, Operation> entry) {
        String operationId = entry.getValue().getOperationId();
        return (operationId != null) ? operationId : entry.getKey().name() + pathname.replaceAll("-", "_").replaceAll("/", "_").replaceAll("[{}]", "");
    }

    protected String extractPortFromHost(String host) {
        if (host != null) {
            int portSeparatorIndex = host.indexOf(':');
            if (portSeparatorIndex >= 0 && portSeparatorIndex + 1 < host.length()) {
                return host.substring(portSeparatorIndex + 1);
            }
        }
        return "8080";
    }

    private String camelizePath(String path) {
        String word = path;
        Pattern p = Pattern.compile("\\{([^/]*)\\}");
        Matcher m = p.matcher(word);
        while (m.find()) {
            word = m.replaceFirst(":" + m.group(1));
            m = p.matcher(word);
        }
        p = Pattern.compile("(_)(.)");
        m = p.matcher(word);
        while (m.find()) {
            word = m.replaceFirst(m.group(2).toUpperCase());
            m = p.matcher(word);
        }
        return word;
    }
}
