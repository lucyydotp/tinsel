package me.lucyydotp.tinsel.gradle

import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

public fun addSpaceToBitmapFont(
    imageFile: InputStream,
    lineCount: Int,
    heightToAdd: Int,
): BufferedImage {
    val srcImage = imageFile.use(ImageIO::read)

    val destImage = BufferedImage(
        srcImage.colorModel,
        srcImage.colorModel.createCompatibleWritableRaster(srcImage.width, srcImage.height + (heightToAdd * lineCount)),
        srcImage.colorModel.isAlphaPremultiplied,
        null
    )
    val lineHeight = srcImage.height / lineCount

    for (i in 0..<lineCount) {
        val child = srcImage.raster.createChild(
            0,
            i * lineHeight,
            srcImage.width,
            lineHeight,
            0,
            0,
            null
        )
        destImage.raster.setDataElements(0, i * (lineHeight + heightToAdd), child)
    }
    return destImage
}
