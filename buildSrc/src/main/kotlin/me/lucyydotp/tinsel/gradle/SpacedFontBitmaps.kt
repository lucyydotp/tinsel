package me.lucyydotp.tinsel.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

private fun addSpaceToBitmapFont(
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

abstract class GenerateFontBitmapsTask : DefaultTask() {
    @get:Input
    abstract val addedSpacing: Property<Int>

    @get:Input
    abstract val inputFiles: MapProperty<String, Int>

    @get:InputFiles
    val input get() = inputFiles.keySet()

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    init {
        outputDirectory.convention(
            project.layout.projectDirectory.dir("build/textures")
        )
    }

    infix fun String.hasLines(lines: Int) {
        inputFiles.put(this, lines)
    }

    @TaskAction
    private fun run() {
        inputFiles.get().forEach { path, lines ->
            val inputFile = project.file(path)
            val img = addSpaceToBitmapFont(
                inputFile.inputStream(),
                lines,
                addedSpacing.get()
            )

            ImageIO.write(img, "png", outputDirectory.file(inputFile.name).get().asFile)
        }
    }
}
