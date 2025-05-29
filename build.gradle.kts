import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "9.0.0-beta4"
    `java-library`
    `maven-publish`
}

group = "rs.jamie"
version = "1.0.0"



repositories {
    mavenCentral()
    maven {
        url = uri("https://libraries.minecraft.net")
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("io.lettuce:lettuce-core:6.6.0.RELEASE")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    compileOnly("org.jetbrains:annotations:24.0.1")
}

publishing {
    publications {
        create<MavenPublication>("headutil") {
            from(components["java"])
            groupId = "rs.jamie"
            artifactId = "headutil"
            version = "${project.version}"
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks {
    val shadowJar = named<ShadowJar>("shadowJar") {
        configurations = listOf(project.configurations.getByName("shadow"))
        archiveFileName.set("PunishBridge.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}