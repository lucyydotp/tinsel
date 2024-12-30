# Tinsel

*aka Tools to Implement Negative Spacing for Elegant Layouts*

Tinsel is a library for Minecraft Java servers to do fun things with text, using negative-space fonts and glyphs with
extreme vertical ascents.

It's made up of two parts:

- A server-side library to generate Adventure components
- A resource pack containing high-ascent and negative-spacing fonts, shipped as both a prebuilt zip file, and a Gradle
  plugin to generate ascent fonts from your own resources

Either can be used independently, but work best when both used together.

## Usage

Tinsel is still very work-in-progress and not yet production-ready. Once the API is more stable, I'll write some proper
docs, but the example server is a good place to start until then.

1. Install the resource pack by running `gradle :example:pack:build` and copying
   `example/pack/build/distributions/tinsel-example-pack.zip` into your client.
    - Alternatively, set the `MINECRAFT_RESOURCE_PACKS_FOLDER`  environment variable to your client's RP folder and run
      `gradle :example:pack:copyToMinecraft`. This is helpful for local pack development.
    - You will need to copy some vanilla assets into the project to build the pack - see
      `resources/src/main/fontBitmaps/minecraft/font/readme.txt`. This will be automated eventually.
2. Start the test plugin server with `gradle :example:plugin:runServer`.
3. Use the `/tinsel` command to trigger examples and see how they work.
