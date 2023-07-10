<<<<<<< HEAD
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikaricp_version: String by project
val commons_codec_version: String by project
=======
val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val hikaricpVersion: String by project
val postgresVersion: String by project
val commonsCodecVersion: String by project
val mockkVersion: String by project
>>>>>>> development

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
<<<<<<< HEAD
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core-jvm:2.2.4")
    implementation("io.ktor:ktor-server-auth-jvm:2.2.4")
    implementation("io.ktor:ktor-server-cors-jvm:2.2.4")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.4")
    implementation("io.ktor:ktor-server-sessions-jvm:2.2.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.4")
    implementation("io.ktor:ktor-server-auth:2.2.4")
    implementation("io.ktor:ktor-server-auth-jwt:2.2.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Database dependencies
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")

    implementation("commons-codec:commons-codec:$commons_codec_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.4")
=======
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")


    // Database dependencies
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("com.zaxxer:HikariCP:$hikaricpVersion")

    // Testing
    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    implementation("io.ktor:ktor-server-core-jvm:2.3.2")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.2")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.2")
    implementation("io.ktor:ktor-server-sessions-jvm:2.3.2")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.2")
    implementation("io.ktor:ktor-server-auth:2.3.2")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.2")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.2")
    implementation("io.ktor:ktor-client-core-jvm:2.3.2")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.2")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.testng:testng:7.7.0")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.2")
>>>>>>> development
}
