import nu.studer.gradle.jooq.JooqEdition
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	id("nu.studer.jooq") version "8.0"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.api"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jooq:jooq")
	runtimeOnly("com.h2database:h2")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// jOOQ
	jooqGenerator("com.h2database:h2")
	jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
}

jooq {
	version.set("3.18.13")
	edition.set(JooqEdition.OSS)
	configurations {
		create("main") {
			jooqConfiguration.apply {
				jdbc.apply {
					driver = "org.h2.Driver"
					url = "jdbc:h2:file:./build/h2/testdb;IFEXISTS=TRUE"
					user = "sa"
					password = ""
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					database.apply {
						name = "org.jooq.meta.h2.H2Database"
						inputSchema = "PUBLIC"
						includes = ".*"
					}
					generate.apply {
						isDeprecated = false
						isRecords = true
						isTables = true
						isImmutablePojos = true
						isFluentSetters = true
					}
					target.apply {
						packageName = "org.jooq.h2.generated"
						directory = "build/generated-sources/jooq/db"
					}
				}
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named("generateJooq").configure {
	if (!file("./build/h2/testdb.mv.db").exists()) {
		enabled = false
	}
}
