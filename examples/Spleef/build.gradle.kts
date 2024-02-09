plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.twelveoclock"
version = "1.0.0"

repositories {

    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/public/") {
        name = "SpigotMC"
    }

    maven("12oclockDev") {
        url = uri("https://maven.12oclock.dev/releases")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("dev.twelveoclock:minigame-engine:1.0.21")
}

tasks {
    shadowJar {
        archiveFileName.set("Spleef.jar")
    }
}