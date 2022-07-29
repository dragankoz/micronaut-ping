package com.company.example.micronaut;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@OpenAPIDefinition(
        info = @Info(
                title = "${api.title}",
                version = "${api.version}",
                description = "${api.description}",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(url = "https://www.company.com.au", name = "Support", email = "support@company.com.au")),
        externalDocs = @ExternalDocumentation(url = "https://tba-dev-portal.company.qld.com.au")
)
@Slf4j
@Controller //(value = "/java")
public class HomeController {

    @Value("${aws.secretid}")
    String awsSecretId;

    @Value("${aws.region}")
    String awsRegion;

    @Operation(summary = "Ping from java", description = "A greeting is returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied"),
            @ApiResponse(responseCode = "404", description = "Something not found"),
    })
    @Tag(name = "Hi there")
    @Get(value = "/hello", produces = MediaType.APPLICATION_JSON)
    public Map<String, Object> get() {
        log.info("Running get()");
        return Collections.singletonMap("message", "Hello from java()");
    }

    @Operation(summary = "Greets a person with a ping", description = "A friendly ping greeting is returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied"),
            @ApiResponse(responseCode = "404", description = "Something not found"),
    })
    @Tag(name = "Java ping")
    @Get(value = "/ping", produces = MediaType.APPLICATION_JSON)
    public Map<String, Object> ping() {
        log.info("Running ping()");
        return Collections.singletonMap("message", "Hello from ping()");
    }

    @Operation(summary = "Greets a person with a secret", description = "A friendly secret greeting is returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied"),
            @ApiResponse(responseCode = "404", description = "Something not found"),
    })
    @Tag(name = "java secret")
    @Get(value = "/secret", produces = MediaType.APPLICATION_JSON)
    public Map<String, Object> secret() {
        log.info("Running secret()");
        return Collections.singletonMap("message", String.format("Hello, secret is [%s]", getSecretValue()));
    }

    public String getSecretValue() {

        String secret;
        Region region = Region.of(awsRegion);
        try (SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build()) {

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(awsSecretId)
                    .build();

            GetSecretValueResponse getSecretValueResponse;

            try {
                getSecretValueResponse = secretsClient.getSecretValue(getSecretValueRequest);
            } catch (Exception e) {
                log.error("Failed to get secret from secrets manager");
                throw e;
            }

            // Decrypts secret using the associated KMS key.
            // Depending on whether the secret is a string or binary, one of these fields will be populated.
            if (getSecretValueResponse.secretString() != null) {
                secret = getSecretValueResponse.secretString();
            } else {
                secret = new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
            }

            return secret;
        } catch (SecretsManagerException e) {
            log.error("Failed to initialize secrets manager");
            throw e;
        }
    }
}

