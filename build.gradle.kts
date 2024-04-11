plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.15"
    //id("com.google.protobuf") version "0.9.4"

}

group = "dev.twelveoclock"
version = "1.0.21"

repositories {

    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/public/") {
        name = "SpigotMC"
    }

    /*
    maven("https://mvn.intellectualsites.com/content/repositories/releases/") {
        name = "FastAsyncWorldEdit"
    }
    */

    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "PaperMC"
    }

}

dependencies {

    //compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT") // Load NMS
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    /*
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:1.17-317")

    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:1.17-317") {
        isTransitive = false
    }*/

    //implementation("com.google.protobuf:protobuf-java:3.25.2")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation(platform("com.intellectualsites.bom:bom-newest:1.43")) // Ref: https://github.com/IntellectualSites/bom

    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.16.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.17:1.13.0")
}

/*
sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
    }
}*/

/*
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
    }
}
*/

tasks {

    test {
        useJUnitPlatform()
    }

    shadowJar {
        relocate("com.fasterxml", "dev.twelveoclock.plugintemplate.libs.com.fasterxml")
        relocate("org.jetbrains", "dev.twelveoclock.plugintemplate.libs.org.jetbrains")
        relocate("org.intellij", "dev.twelveoclock.plugintemplate.libs.org.intellij")
        archiveFileName.set("MiniGameEngine.jar")
    }
}

publishing {
    repositories {
        maven {
            name = "12oclockDev"
            url = uri("https://maven.12oclock.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.twelveoclock"
            artifactId = "minigame-engine"
            from(components["java"])
        }
    }
}