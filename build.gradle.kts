plugins {
	`kotlin-dsl`
	`maven-publish`
	id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
}

version = project.properties["version"].toString()
group = project.properties["group"].toString()

base {
	archivesName = project.name
}

loom {
	mods {
		register(project.name) {
			sourceSet(sourceSets["main"])
		}
	}
}

//repositories {
//	maven("https://maven.hugeblank.dev/releases") {
//		content {
//			includeGroup("dev.hugeblank")
//			includeGroup("cc.tweaked")
//		}
//	}
//	maven("https://basique.top/maven/releases") {
//		content {
//			includeGroup("me.basiqueevangelist")
//		}
//	}
//}

dependencies {
	minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
	implementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks {

	processResources {
		inputs.property("version", project.version)
		inputs.property("name", project.name)

		filesMatching("manifest.json") {
			expand(mutableMapOf("version" to project.version, "name" to project.name))
		}
	}
}
