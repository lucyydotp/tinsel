plugins {
    id("me.lucyydotp.tinsel")
}

tinsel.fonts {
    create("tinsel:default") {
        offsets = setOf(
            -4, -8, -12, -16, -20, -24, -28, -32, -36, -40, -44, -48,
            4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48
        )
        addedBitmapSpacing = 48
    }
}
