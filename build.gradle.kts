plugins {
    kotlin("jvm") version "1.7.21"
    application

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.akkih.kobot"

allprojects {
    version = "1.0"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.3")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")

    implementation("org.slf4j:slf4j-simple:2.0.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("me.akkih.kobot.AppBootstrapKt")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    build {
        dependsOn(shadowJar)
    }

    jar {
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }

    test {
        useJUnitPlatform()
        doFirst {
            val fileName = if(System.getenv("PROD") == "true") ".env.prod" else ".env.dev"
            if(!file(fileName).exists()) return@doFirst
            file(fileName).forEachLine {
                val variable = it.replace("\"", "").split("=", limit = 2)
                if(variable.size > 1) {
                    environment(variable[0], variable[1])
                }
            }
        }
    }

    withType(JavaExec::class) {
        doFirst {
            val fileName = if(System.getenv("PROD") == "true") ".env.prod" else ".env.dev"
            if(!file(fileName).exists()) return@doFirst
            file(fileName).forEachLine {
                val variable = it.replace("\"", "").split("=", limit = 2)
                if(variable.size > 1) {
                    environment(variable[0], variable[1])
                }
            }
        }
    }
}