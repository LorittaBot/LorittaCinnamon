package net.perfectdreams.loritta.commands.images

import net.perfectdreams.loritta.api.command.Command
import net.perfectdreams.loritta.api.command.CommandCategory
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.utils.Constants
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class SAMCommand : Command("sam", arrayOf("southamericamemes"), category = CommandCategory.IMAGES) {
	companion object {
		val SELO_SAM by lazy { ImageIO.read(File(Constants.ASSETS_FOLDER, "selo_sam.png")) }
	}

	@Subcommand
	suspend fun root(context: CommandContext) {
		var div: Double? = 1.5

		if (context.args.size >= 2) {
			div = context.args[1].toDoubleOrNull()
		}

		if (div == null) {
			div = 1.5
		}

		val image = context.getImageAt(0) ?: run { Constants.INVALID_IMAGE_REPLY.invoke(context); return; }

		var seloSouthAmericaMemes: Image = SELO_SAM

		val height = (image.height / div).toInt() // Baseando na altura
		seloSouthAmericaMemes = seloSouthAmericaMemes.getScaledInstance(height, height, Image.SCALE_SMOOTH)

		val x = Constants.random.nextInt(0, Math.max(1, image.width - seloSouthAmericaMemes.getWidth(null)))
		val y = Constants.random.nextInt(0, Math.max(1, image.height - seloSouthAmericaMemes.getHeight(null)))

		image.graphics.drawImage(seloSouthAmericaMemes, x, y, null)

		context.reply(
				image,
				"#loritta"
		).queue()
	}
}