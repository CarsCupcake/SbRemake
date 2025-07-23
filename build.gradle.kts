import org.gradle.internal.os.OperatingSystem

plugins {
    java
    application
    `java-library`
    id("com.gradleup.shadow") version "9.0.0-rc1"
}

group = "me.carscupcake"
version = "0.0.10"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
        sourceCompatibility = JavaVersion.VERSION_23
    }
}
application {
    mainClass.set("me.carscupcake.sbremake.Main")
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://libraries.minecraft.net")
}


dependencies {
    platform("com.intellectualsites.bom:bom-1.18.x:1.12")
    platform("org.jboss.shrinkwrap:shrinkwrap-bom:1.2.6")
    implementation("net.kyori:adventure-api:4.21.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.21.0")
    implementation("net.kyori:adventure-text-serializer-json:4.21.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.1")
    implementation("dev.hollowcube:polar:1.7.2")
    implementation(project(":minestom"))
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("log4j:log4j:1.2.17")
    implementation("org.jooq:joor:0.9.15")
    implementation("org.apache.cassandra:cassandra-all:5.0.3")
    implementation("org.kohsuke:github-api:1.326")
    implementation("net.sf.sevenzipjbinding:sevenzipjbinding:16.02-2.01")
    implementation("net.sf.sevenzipjbinding:sevenzipjbinding-all-platforms:16.02-2.01")
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")
    implementation("org.apache.commons:commons-csv:1.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.19.0")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "me.carscupcake.sbremake.Main", // Sets the main class for execution
            "Implementation-Title" to "SbRemake",
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "CarsCupcake",
            "Specification-Title" to "SbRemake",
            "Specification-Version" to project.version,
            "Specification-Vendor" to "CarsCupcake",
            "Multi-Release" to "true" // Indicates a multi-release JAR
        )
    }
}
tasks.shadowJar {
    archiveFileName.set("SbRemake.jar")
    destinationDirectory.set(project.file("target"))
}

sourceSets.main {
    resources {
        srcDirs("src/main/resources")
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
