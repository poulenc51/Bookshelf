import nu.studer.gradle.jooq.JooqEdition
import org.jetbrains.kotlin.cli.jvm.main
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
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jooq:jooq")
	implementation("org.postgresql:postgresql")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
	testImplementation("org.assertj:assertj-core:3.25.3")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

	// jOOQ
	jooqGenerator("org.postgresql:postgresql:42.7.2")
	jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
}

jooq {
	version.set("3.17.22")
	edition.set(JooqEdition.OSS)
	configurations {
		create("main") {
			generateSchemaSourceOnCompilation.set(true)
			jooqConfiguration.apply {
				jdbc.apply {
					url = "jdbc:postgresql://localhost:5432/main"
					user = "postgres"
					password = "postgres"
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						inputSchema = "public"
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
						packageName = "org.jooq.postgresql.generated"
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
