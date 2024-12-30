plugins {
    id("me.lucyydotp.tinsel")
}

tinsel.packArchiveName = "tinsel-example-pack.zip"

dependencies {
    // Tinsel lets you include contents from other packs built through the plugin.
    // In practice, you probably won't want to do this - if you want a custom pack,
    // you should create your own by copying the `resources` folder at the root of
    // the project. This is helpful when you want to reuse content or configuration
    // across multiple packs.
    resourcePack(project(":resources"))
}
