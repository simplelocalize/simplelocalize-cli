# How to contribute

## How to build a project

Java programs are packaged in a JAR files. To build a project to a Java file you need to have installed Java 11+ (`java --version`) and Maven 3+ (`mvn --version`)

### Installation
The easiest and quickes way of installing Java and Maven is using [SDKMAN](https://sdkman.io) project.

#### Install SDKMAN
`curl -s "https://get.sdkman.io" | bash`

#### Install Java
`sdk install java`

#### Install Maven
Maven is a package manager for Java projects.

`sdk install maven`

### Build project

To build a project run:

`mvn clean install --DskipTests --file pom.xml`

Output JAR file will appear in `target` directory.

### Run JAR file

`java -jar target/simplelocalize-cli-2.0.3.jar`

You can pass arguments to CLI by adding them to the end of the command:

`java -jar target/simplelocalize-cli-2.0.3.jar download`


### Run tests
To run project tests you can simply skip `--DskipTests` flag.

`mvn clean install --file pom.xml`

## How to build binaries

The best way to build binary files is to run a Github Action [build.yaml](https://github.com/simplelocalize/simplelocalize-cli/blob/c9bb9512465d815d89ca1b4cc38d13c542eecac7/.github/workflows/build.yml) workflow

### Quick guide
- Fork the repository
- In forked repo, head to 'Actions' tab and enable Github Actions
- Make a commit to invoke a new build action
- Wait for successful build and download executables

> Build might take some time, usually it takes less than ~20 minutes.

![Where to find Github Actions artifacts](https://github.com/simplelocalize/simplelocalize-cli/blob/ac132cbc130d8654c18d3dc26b6316153e9e8ed0/artifacts.png)
