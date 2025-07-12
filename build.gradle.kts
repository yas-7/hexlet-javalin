import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
	id("io.freefair.lombok") version "8.13.1"
}

application {
	mainClass.set("org.example.hexlet.HelloWorld")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	// Актуальные версии зависимостей
	implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
	implementation("io.javalin:javalin:6.1.3")           // версия фреймворка Javalin
	implementation("org.slf4j:slf4j-simple:2.0.7")       // простая реализация SLF4J
	implementation("io.javalin:javalin-rendering:6.1.3") // библиотека для рендеринга шаблонов
	implementation("gg.jte:jte:3.1.9")                  // JTE — фреймворк для рендеринга шаблонов
	testImplementation(platform("org.junit:junit-bom:5.9.1")) // Тестовая инфраструктура JUnit
	testImplementation("org.junit.jupiter:junit-jupiter") // Библиотека юнит-тестов JUnit Jupiter
}

tasks.test {
	useJUnitPlatform()
	// https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		// showStackTraces = true
		// showCauses = true
		showStandardStreams = true
	}
}
