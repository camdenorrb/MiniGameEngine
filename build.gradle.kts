plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "dev.twelveoclock"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
}

dependencies {

    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")

    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("org.jetbrains:annotations:22.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.17:1.10.1")
}


tasks {
    test {
        useJUnitPlatform()
    }
}