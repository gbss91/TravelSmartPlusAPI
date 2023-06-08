val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val hikaricpVersion: String by project
val postgresVersion: String by project
val commonsCodecVersion: String by project
val mockkVersion: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.2.0"
}

group = "com.travelsmartplus"
version = "0.0.1"

application {
    mainClass.set("com.travelsmartplus.ApplicationKt")
}

ktlint {
    disabledRules.set(setOf("no-wildcard-imports"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.ktor:ktor-server-core-jvm:2.3.1")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.1")
    implementation("io.ktor:ktor-server-sessions-jvm:2.3.1")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.1")
    implementation("io.ktor:ktor-server-auth:2.3.1")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.1")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.1")
    implementation("io.ktor:ktor-client-core-jvm:2.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    // HttpClient for external API calls


    // Database dependencies
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("com.zaxxer:HikariCP:$hikaricpVersion")

    // Testing
    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    testImplementation("org.testng:testng:7.7.0")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.1")
}
