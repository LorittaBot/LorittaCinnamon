import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3-M2"
}

group = "net.perfectdreams.loritta"
version = "2018.09.28-SNAPSHOT"

repositories {
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap") }
    mavenCentral()
}

dependencies {
	compile(project(":loritta-core"))
    compile(kotlin("stdlib-jdk8"))
    compile("org.twitter4j", "twitter4j-core", "4.0.7")
    compile("org.twitter4j", "twitter4j-stream", "4.0.7")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}