import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("jvm") version "1.6.21"
    id("org.jsonschema2pojo") version "1.1.3"

    application
    groovy

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.7.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-test:2.7.0")
    implementation("org.testcontainers:testcontainers:1.15.1")
    implementation("org.testcontainers:mongodb:1.15.1")

    testImplementation("org.jsonschema2pojo:jsonschema2pojo-gradle-plugin:1.1.3")
    testImplementation("org.jsonschema2pojo:jsonschema2pojo-core:1.0.2")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.11.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.0")
    testImplementation("org.codehaus.groovy:groovy:3.0.12")
    testImplementation("org.spockframework:spock-core:2.1-groovy-3.0")
    testImplementation("org.spockframework:spock-spring:2.1-groovy-3.0")

}

tasks.test {
    useJUnitPlatform()
}
sourceSets{
    test{
        groovy.srcDirs("src/test/kotlin",
            "src/intTest"
        )

        resources{
            srcDirs(
                "src/intTest/resources"
            )
        }
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
task generatePojo(type: org.jsonschema2pojo.Jsonschema2PojoTask) {
    source = files("src/main/resources/sample.json")
    targetDirectory = file("src/main/java")
    usePrimitives = true
    includeAdditionalProperties = false
    includeDynamicAccessors = true
}

