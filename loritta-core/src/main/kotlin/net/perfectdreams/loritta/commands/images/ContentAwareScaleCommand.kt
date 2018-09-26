package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.SeamCarver
import net.perfectdreams.loritta.api.command.Command
import net.perfectdreams.loritta.api.command.CommandCategory
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.utils.Constants
import net.perfectdreams.loritta.utils.LorittaImage

class ContentAwareScaleCommand : Command("cas", arrayOf("contentawarescale"), category = CommandCategory.IMAGES) {
	@Subcommand
	suspend fun root(context: CommandContext) {
		var contextImage = context.getImageAt(0) ?: run { Constants.INVALID_IMAGE_REPLY.invoke(context); return; }

		val loriImage = LorittaImage(contextImage)
		loriImage.resize(512, 512, true)
		contextImage = loriImage.bufferedImage

		var newImage = contextImage

		for (i in 0..399) {
			// determine scale
			var scaleTo = if (Constants.random.nextBoolean()) "horizontal" else "vertical"

			if (200 > newImage.height) { // se ficar menos que 200 irá ficar bem ruim a imagem
				scaleTo = "vertical"
			}

			if (200 > newImage.width) { // se ficar menos que 200 irá ficar bem ruim a imagem
				scaleTo = "horizontal"
			}

			if (100 > newImage.height) { // se ficar menos que 200 irá ficar bem ruim a imagem
				break
			}

			if (100 > newImage.width) { // se ficar menos que 200 irá ficar bem ruim a imagem
				break
			}

			// Get the new image w/o one seam.
			newImage = SeamCarver.carveSeam(newImage, scaleTo)
		}

		context.reply(
				newImage,
				"#loritta"
		).queue()
	}
}