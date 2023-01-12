plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.twelveoclock"
version = "1.0.0"

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

    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    /*
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:1.17-317")

    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:1.17-317") {
        isTransitive = false
    }*/

    implementation("org.jetbrains:annotations:23.1.0")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.13.4")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
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
    }
}

tasks {

}