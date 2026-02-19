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

repositories {
	maven("https://maven.fabricmc.net") {
		content {
			includeGroup("net.fabricmc")
		}
	}
	maven("https://maven.moongarden.dev/releases") {
		content {
			includeGroup("dev.moongarden")
			includeGroup("cc.tweaked")
		}
	}
	maven("https://maven.moongarden.dev/snapshots") {
		content {
			includeGroup("dev.moongarden")
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
	implementation("dev.moongarden:combine:${project.properties["combine_version"]}")
	implementation("dev.moongarden:allium:${project.properties["allium_version"]}")
	implementation("dev.moongarden:bouquet:${project.properties["bouquet_version"]}")
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

	var output = "-Dcombine.output=../docs"
	var extensions = "-Dcombine.extensions=dev.moongarden.allium.util.combine.AlliumCombineExtension"
	register<JavaExec>("genLuaSources") {
		group = "allium"

		classpath = sourceSets["main"].runtimeClasspath
		mainClass = "dev.moongarden.combine.Combine"
		jvmArgs = listOf(output, extensions)
		workingDir = file("run")
	}

	register<Exec>("updateGlobals") {
		group = "allium"
		commandLine = listOf("git", "submodule", "update", "--recursive")
	}

	// Uncomment to create task that reveals private/protected methods and fields in the generated Lua documentation
//	register<JavaExec>("genLuaSourcesAll") {
//		group = "allium"
//
//		classpath = sourceSets["main"].runtimeClasspath
//		mainClass = "dev.moongarden.combine.Combine"
//		jvmArgs = listOf(output, extensions, "-Dcombine.ignoreAccess")
//		workingDir = file("run")
//	}

	register<Zip>("zip") {
		group = "build"
		archiveFileName = "${project.properties["id"]}-${project.properties["version"]}.zip"
		destinationDirectory = layout.buildDirectory.dir("script")
		from(sourceSets.main.get().output)
		from(layout.buildDirectory.dir("resources/main/scripts/${project.properties["id"]}"))
		finalizedBy("cleanResources")
	}

	register<Delete>("cleanResources") {
		delete(layout.buildDirectory.dir("resources"))
	}
}
