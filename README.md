# Allium Example Script

A fork of the [Fabric Example Mod](https://github.com/FabricMC/fabric-example-mod), redesigned to aid in Allium scripting.

## Setup

For setup instructions please see the [Allium documentation page](https://moongardenmods.github.io/allium-docs/setup/get-started.html) that relates to the IDE that you are using.

Generally:
1. Run the `gradle updateGlobals` task. This will fetch documentation for allium's global documentation, which are also
used in the next step.
2. Run the `gradle genLuaSources` task. This is for lua definitions of game sources for easy tab completion.
3. Set the script ID, version, and name in the `gradle.properties`.
4. Navigate to the script's directory at `src/main/resources/scripts`
5. Rename the `script_id` directory to the ID that was set in `gradle.properties` in step 2

To test, launch the `Minecraft Client` IDEA task (or representative task in your respective IDE).

To build, run the `gradle build` task. A distributable zip archive is created in the `build/script` directory.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
