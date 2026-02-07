# Allium Example Script

## Setup

For setup instructions please see the [Allium documentation page](https://moongardenmods.github.io/allium-docs/setup/get-started.html) that relates to the IDE that you are using.

Generally:
1. Run the `gradle runDatagen` task. This is for lua defintions of game sources.
2. Set the script ID, version, and name in the `gradle.properties`.
3. Navigate to the script's directory at `src/main/resources/scripts`
4. Rename the `script_id` directory to the ID that was set in `gradle.properties` in step 2

To test, launch the `Minecraft Client` IDEA task (or representative task in your respective IDE).

To build, launch the `gradle build` task. A distributable zip archive is created in the `build/script` directory.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
