/*
    You can add your module specific dependencies here
 */
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-core:7.2.0-SNAPSHOT")
}

repositories {
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven("https://mvn.intellectualsites.com/content/repositories/releases/")
    maven("https://maven.enginehub.org/repo/")
}

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}