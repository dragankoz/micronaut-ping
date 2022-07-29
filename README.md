## Micronaut Ping Lambda Example

### Micronaut reading

- [Deploy a Serverless Micronaut function to AWS Lambda Java 11 Runtime](https://guides.micronaut.io/latest/mn-serverless-function-aws-lambda-maven-java.html)
- [User Guide](https://docs.micronaut.io/3.5.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.5.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.5.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Prereqisites:

#### If using WSL2
- Sdkman
- Graalvm 22.1.0.1 (22.1.0.1.r11-gln)+
- Maven (3.6.3)+
- NodeJS (16.x)+
- [Serverless framework](https://www.serverless.com/framework/docs/getting-started)

#### If using Windows only and Docker for native image generation
- JDK 11+
- Maven (3.6.3)+
- NodeJS  (16.x)+
- [Docker desktop](https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe)
- [Serverless framework](https://www.serverless.com/framework/docs/getting-started)

## Required Configuration
You will need to setup some config/credentials for AWS access. This is typically 2 files which are kept in,


*$HOME/.aws/credentials*
```
[default]
aws_access_key_id = AKIAVFCV5QAXXXXXXXXX
aws_secret_access_key = +PDrJJ0+xhTHSs6TZEvJd/XXXXXXXXXXXXXXXXXX
                                              
```

### Setup
Options explained
For compiling Java down to native executables in Windows, this can be done in 2 ways, using either WSL2 within windows or by indirectly using Docker desktop, which will be invoked during the windows maven build.

### Install/Setup WSL2

- [Install WSL](https://docs.microsoft.com/en-us/windows/wsl/install)

- Turn off Windows Path inclusion on WSL

```
C:\> wsl
# vi /etc/wsl.conf
[interop]
enabled=false # enable launch of Windows binaries; default is true
appendWindowsPath=false # append Windows path to $PATH variable; default is true

C:\> wsl --shutdown
```

- Install sdkman.io

```
C:\> wsl
# apt update
# apt -y upgrade
# apt -y install curl
# curl -s "https://get.sdkman.io" | bash
# sdk install java 22.1.0.1.r11-gln
# sdk install maven 3.6.3
```

- Install native image creation
```
C:\> wsl
# apt-get -y install build-essential libz-dev zlib1g-dev
```

## Build and deploy to AWS (JVM)
```
mvn clean package
serverless plugin install -n serverless-better-credentials
serverless plugin install -n serverless-iam-roles-per-function
serverless plugin install -n serverless-openapi-integration-helper
serverless deploy
```
This will build the a standard far jar `function.jar` ready for the deployment.


## Build and deploy to AWS (Native image)
```
mvn clean package -Dpackaging=docker-native -Pgraalvm
serverless plugin install -n serverless-better-credentials
serverless plugin install -n serverless-iam-roles-per-function
serverless plugin install -n serverless-openapi-integration-helper
serverless deploy
```
This will build the GraalVM native image inside a docker container and generate the `function.zip` ready for the deployment.


## Handler

[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)

Handler: io.micronaut.function.aws.proxy.MicronautLambdaHandler

## Feature aws-lambda-custom-runtime documentation

- [Micronaut Custom AWS Lambda runtime documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes)

- [https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html)


## Feature aws-lambda documentation

- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)


## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)


