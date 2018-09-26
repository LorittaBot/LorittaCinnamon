package net.perfectdreams.loritta.commands.images

import net.perfectdreams.loritta.api.command.Command
import net.perfectdreams.loritta.api.command.CommandCategory
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.utils.Constants
import net.perfectdreams.loritta.utils.LorittaImage
import net.perfectdreams.loritta.utils.extensions.toBufferedImage
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class AtaCommand : Command("ata", category = CommandCategory.IMAGES) {
	companion object {
		val TEMPLATE by lazy { ImageIO.read(File(Constants.ASSETS_FOLDER, "ata.png")) }
	}

	@Subcommand
	suspend fun root(context: CommandContext) {
		val contextImage = context.getImageAt(0) ?: run { Constants.INVALID_IMAGE_REPLY.invoke(context); return; }
		val template = TEMPLATE
		val base = BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB)
		val scaled = contextImage.getScaledInstance(300, 300, BufferedImage.SCALE_SMOOTH).toBufferedImage()

		val transformed = LorittaImage(scaled)
		transformed.setCorners(107F, 0F,
				300F, 0F,
				300F, 177F,
				96F, 138F)

		base.graphics.drawImage(transformed.bufferedImage, 0, 0, null)
		base.graphics.drawImage(template, 0, 0, null)

		context.reply(base, "#loritta").queue()
	}
}