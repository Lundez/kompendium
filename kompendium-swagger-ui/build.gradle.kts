plugins {
  `java-library`
  `maven-publish`
  signing
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(libs.bundles.ktor)
  api(libs.ktor.webjars)
  implementation(libs.webjars.swagger.ui)
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
  testImplementation("io.ktor:ktor-server-test-host:1.5.3")
}

java {
  withSourcesJar()
  withJavadocJar()
}

publishing {
  repositories {
    maven {
      name = "GithubPackages"
      url = uri("https://maven.pkg.github.com/bkbnio/kompendium")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("kompendium") {
      from(components["kotlin"])
      artifact(tasks.sourcesJar)
      artifact(tasks.javadocJar)
      groupId = project.group.toString()
      artifactId = project.name.toLowerCase()
      version = project.version.toString()

      pom {
        name.set("Kompendium")
        description.set("A minimally invasive OpenAPI spec generator for Ktor")
        url.set("https://github.com/bkbnio/Kompendium")
        licenses {
          license {
            name.set("MIT License")
            url.set("https://mit-license.org/")
          }
        }
        developers {
          developer {
            id.set("bkbnio")
            name.set("Ryan Brink")
            email.set("admin@bkbn.io")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/bkbnio/Kompendium.git")
          developerConnection.set("scm:git:ssh://github.com/bkbnio/Kompendium.git")
          url.set("https://github.com/bkbnio/Kompendium.git")
        }
      }
    }
  }
}

signing {
  val signingKey: String? by project
  val signingPassword: String? by project
  useInMemoryPgpKeys(signingKey, signingPassword)
  sign(publishing.publications)
}
