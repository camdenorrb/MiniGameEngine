plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.twelveoclock"
version = "1.0.12"

repositories {

    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/public/") {
        name = "SpigotMC"
    }

    maven("https://mvn.intellectualsites.com/content/repositories/releases/") {
        name = "FastAsyncWorldEdit"
    }
}

dependencies {

    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    /*
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:1.17-317")

    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:1.17-317") {
        isTransitive = false
    }*/

    implementation("org.jetbrains:annotations:24.0.1")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.15.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.17:1.13.0")
}


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