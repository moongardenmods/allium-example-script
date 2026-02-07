plugins {
	`maven-publish`
	id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
}

version = project.properties["version"].toString()

loom {
	mods {
		register(project.name) {
			sourceSet(sourceSets["main"])
		}
	}
}

fabricApi {
	configureDataGeneration() {
		client = true
	}
}

repositories {
	maven("https://maven.fabricmc.net") {
		content {
			includeGroup("net.fabricmc")
		}
	}
	maven("https://maven.hugeblank.dev/releases") {
		content {
			includeGroup("dev.hugeblank")
			includeGroup("cc.tweaked")
		}
	}
	maven("https://maven.hugeblank.dev/snapshots") {
		content {
			includeGroup("dev.hugeblank")
			includeGroup("cc.tweaked")
		}
	}
	maven("https://basique.top/maven/releases") {
		content {
			includeGroup("me.basiqueevangelist")
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
	implementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
	implementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_api_version"]}")
	implementation("dev.hugeblank:allium:${project.properties["allium_version"]}")
	implementation("dev.hugeblank:bouquet:${project.properties["bouquet_version"]}")
	implementation("me.basiqueevangelist:enhanced-reflection:${project.properties["enhanced_reflection_version"]}")
}

tasks {
	processResources {
		inputs.property("version", project.version)
		inputs.property("name", project.properties["name"])
		inputs.property("id", project.properties["id"])


		filesMatching("fabric.mod.json") {
			expand(mutableMapOf("id" to project.properties["id"]))
		}

		filesMatching("scripts/*/manifest.json") {
			expand(mutableMapOf("version" to project.version, "name" to project.properties["name"], "id" to project.properties["id"]))
		}
	}

	assemble {
		dependsOn("zip")
	}

	jar {
		enabled = false
	}

	register<Zip>("zip") {
		group = "build"
		archiveFileName = "${project.properties["id"]}-${project.properties["version"]}.zip"
		destinationDirectory = layout.buildDirectory.dir("libs")
		from(sourceSets.main.get().output)
		from(layout.buildDirectory.dir("resources/main/scripts/${project.properties["id"]}"))
		finalizedBy("cleanResources")
	}

	register<Delete>("cleanResources") {
		delete(layout.buildDirectory.dir("resources"))
	}
}
