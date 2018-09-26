package net.perfectdreams.loritta.commands.images

import net.perfectdreams.loritta.api.command.Command
import net.perfectdreams.loritta.api.command.CommandCategory
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.utils.Constants
import net.perfectdreams.loritta.utils.LorittaImage
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class RomeroBrittoCommand : Command("romerobritto", arrayOf("pintura", "painting"), category = CommandCategory.IMAGES) {
	companion object {
		val TEMPLATE by lazy { ImageIO.read(File(Constants.ASSETS_FOLDER, "romero_britto.png")) }
	}

	@Subcommand
	suspend fun root(context: CommandContext) {
		val source = context.getImageAt(0) ?: run { Constants.INVALID_IMAGE_REPLY.invoke(context); return; }

		val image = BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB)

		val graphics = image.graphics
		val skewed = LorittaImage(source)

		skewed.resize(300, 300)

		// skew image
		skewed.setCorners(
				// keep the upper left corner as it is
				16F,19F, // UL

				201F,34F, // UR

				208F,218F, // LR

				52F, 294F); // LL

		graphics.drawImage(skewed.bufferedImage, 0, 0, null)

		graphics.drawImage(TEMPLATE, 0, 0, null) // Desenhe o template por cima!
		println("Sending!")

		val os = ByteArrayOutputStream()
		ImageIO.write(image, "png", os)

		context.reply(
				image, "#loritta"
		).queue()
	}
}