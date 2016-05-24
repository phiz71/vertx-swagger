package io.vertx.ext.swagger.codegen;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

public class JavaVertXServerGenerator extends JavaClientCodegen implements CodegenConfig {

    // source folder where to write the files
    protected String sourceFolder = "src/main/java";
    protected String resourceFolder = "src/main/resources";
    protected String rootPackage = "io.swagger.server.api";
    protected String apiVersion = "1.0.0-SNAPSHOT";

    public static final String ROOT_PACKAGE = "rootPackage";
    public static final String VERTICLE_PACKAGE = "verticlePackage";

    protected String verticlePackage = "";

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

    public JavaVertXServerGenerator() {
        super();

        // set the output folder here
        outputFolder = "generated-code/javaVertXServer";

        /**
         * Models. You can write model files using the modelTemplateFiles map.
         * if you want to create one template for file, you can do so here. for
         * multiple files for model, just put another entry in the
         * `modelTemplateFiles` with a different extension
         */

        modelTemplateFiles.clear();
        modelTemplateFiles.put("model.mustache", // the template to use
                ".java"); // the extension for each file to write

        /**
         * Api classes. You can write classes for each Api file with the
         * apiTemplateFiles map. as with models, add multiple entries with
         * different extensions for multiple files per class
         */
        apiTemplateFiles.clear();
        apiTemplateFiles.put("api.mustache", // the template to use
                ".java"); // the extension for each file to write
        apiTemplateFiles.put("apiVerticle.mustache", // the template to use
                "Verticle.java"); // the extension for each file to write

        /**
         * Template Location. This is the location which templates will be read
         * from. The generator will use the resource stream to attempt to read
         * the templates.
         */
        embeddedTemplateDir = templateDir = "javaVertXServer";

        /**
         * Api Package. Optional, if needed, this can be used in templates
         */
        apiPackage = rootPackage + ".verticle";

        /**
         * Model Package. Optional, if needed, this can be used in templates
         */
        modelPackage = rootPackage + ".model";

        additionalProperties.put(ROOT_PACKAGE, rootPackage);

        groupId = "io.swagger";
        artifactId = "swagger-java-vertx-server";
        artifactVersion = apiVersion;
        
        

    }

    @Override
    public void processOpts() {
        super.processOpts();

        apiTestTemplateFiles.clear();

        importMapping.remove("ApiModelProperty");
        importMapping.remove("ApiModel");
        importMapping.put("JsonInclude", "com.fasterxml.jackson.annotation.JsonInclude");

        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();

        supportingFiles.clear();
        supportingFiles.add(new SupportingFile("swagger.mustache", resourceFolder, "swagger.json"));

        supportingFiles.add(new SupportingFile("MainApiVerticle.mustache", sourceFolder + File.separator + rootPackage.replace(".", File.separator), "MainApiVerticle.java"));

        writeOptional(outputFolder, new SupportingFile("vertx-default-jul-logging.mustache", resourceFolder, "vertx-default-jul-logging.properties"));
        writeOptional(outputFolder, new SupportingFile("pom.mustache", "", "pom.xml"));
        writeOptional(outputFolder, new SupportingFile("README.mustache", "", "README.md"));
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);

        if (!BooleanUtils.toBoolean(model.isEnum)) {
            model.imports.remove("ApiModelProperty");
            model.imports.remove("ApiModel");
            model.imports.add("JsonInclude");
        }

        return;
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        objs = super.postProcessOperations(objs);

        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
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
        return objs;
    }

    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);

        String swaggerDef = Json.pretty(swagger);
        this.additionalProperties.put("fullSwagger", swaggerDef);

        String host = swagger.getHost();
        String port = "8080";
        if (host != null) {
            String[] parts = host.split(":");
            if (parts.length > 1) {
                port = parts[1];
            }
        }
        this.additionalProperties.put("serverPort", port);

        if(swagger.getInfo() !=null && swagger.getInfo().getVersion() != null) 
            artifactVersion = apiVersion = swagger.getInfo().getVersion();
        else 
            artifactVersion = apiVersion;
        
        String serviceIdTemp = "";
        if (swagger != null && swagger.getPaths() != null) {
            for (String pathname : swagger.getPaths().keySet()) {
                Path path = swagger.getPath(pathname);
                if (path.getOperationMap() != null) {
                    for (HttpMethod httpMethod : path.getOperationMap().keySet()) {
                        Operation operation = path.getOperationMap().get(httpMethod);
                        // if (operation.getTags() != null)
                        // operation.getTags().clear();
                        // serviceIdTemp =
                        // camelize(httpMethod.name().toLowerCase() + " " +
                        // pathname);
                        // operation.addTag(serviceIdTemp);

                        serviceIdTemp = httpMethod.name() + pathname.replaceAll("-", "_").replaceAll("/", "_").replaceAll("[{}]", "");
                        operation.setVendorExtension("x-serviceId", serviceIdTemp);
                        operation.setVendorExtension("x-serviceId-varName", serviceIdTemp.toUpperCase() + "_SERVICE_ID");
                    }
                }
            }
        }
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
