package net.perfectdreams.loritta.commands.images

import net.perfectdreams.loritta.api.command.Command
import net.perfectdreams.loritta.api.command.CommandCategory
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.utils.Constants
import net.perfectdreams.loritta.utils.ImageUtils
import net.perfectdreams.loritta.utils.LorittaUtils
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class AmizadeCommand : Command("friendship", arrayOf("amizade"), category = CommandCategory.IMAGES) {
	companion object {
		val TEMPLATE_OVERLAY by lazy { ImageIO.read(File(Constants.ASSETS_FOLDER, "amizade_overlay.png")) }
	}

	@Subcommand
	suspend fun root(context: CommandContext) {
		// Não podemos usar context...
		val user = context.getUserAt(0) ?: run {
			context.reply(
					"Usuário 1 inválido!"
			).queue()
			return
		}
		val user2 = context.getUserAt(1) ?: run {
			context.reply(
					"Usuário 1 inválido!"
			).queue()
			return
		}

		val avatar = LorittaUtils.downloadImage(context.user.avatarUrl) ?: run {
			Constants.INVALID_IMAGE_REPLY.invoke(context)
			return
		}
		val avatar2 = LorittaUtils.downloadImage(user.avatarUrl) ?: run {
			Constants.INVALID_IMAGE_REPLY.invoke(context)
			return
		}
		val avatar3 = LorittaUtils.downloadImage(user2.avatarUrl) ?: run {
			Constants.INVALID_IMAGE_REPLY.invoke(context)
			return
		}

		val template = ImageIO.read(File(Constants.ASSETS_FOLDER, "amizade.png")); // Template

		val graphics = template.graphics as Graphics2D // É necessário usar Graphics2D para usar gradients

		// Colocar todos os avatares
		graphics.drawImage(avatar.getScaledInstance(108, 108, BufferedImage.SCALE_SMOOTH), 55, 10, null)
		graphics.drawImage(avatar3.getScaledInstance(110, 110, BufferedImage.SCALE_SMOOTH), 232, 54, null)
		graphics.drawImage(avatar2.getScaledInstance(85, 134, BufferedImage.SCALE_SMOOTH), 0, 166, null)
		graphics.drawImage(avatar2.getScaledInstance(111, 120, BufferedImage.SCALE_SMOOTH), 289, 180, null)

		// E colocar o overlay da imagem
		val overlay = TEMPLATE_OVERLAY // Template
		graphics.drawImage(overlay, 0, 0, null)

		var font = graphics.font.deriveFont(21F)
		graphics.font = font
		graphics.setRenderingHint(
				java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
				java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		var fontMetrics = graphics.getFontMetrics(font)

		val friendshipEnded = "A amizade com ${user.name}"

		var gp = GradientPaint(
				0.0f, 0.0f,
				Color(202, 72, 15),
				0.0f, fontMetrics.getHeight().toFloat() + fontMetrics.getHeight().toFloat(),
				Color(66, 181, 33));
		graphics.paint = gp;

		ImageUtils.drawCenteredStringOutlined(graphics, friendshipEnded, Rectangle(0, 10, 400, 30), font);
		graphics.color = Color.RED

		font = font.deriveFont(30F);
		graphics.font = font

		ImageUtils.drawCenteredStringOutlined(graphics, "acabou", Rectangle(0, 30, 400, 40), font);

		font = font.deriveFont(24F)
		graphics.font = font
		fontMetrics = graphics.getFontMetrics(font)
		gp = GradientPaint(
				0.0f, 140f,
				Color(206, 7, 129),
				0.0f, 190f,
				Color(103, 216, 11));
		graphics.paint = gp;
		// graphics.fillRect(0, 0, 400, 300); // debugging
		ImageUtils.drawCenteredStringOutlined(graphics, "Agora ${user2.name}", Rectangle(0, 100, 400, 110), font);
		ImageUtils.drawCenteredStringOutlined(graphics, "é meu/minha", Rectangle(0, 120, 400, 130), font);
		graphics.color = Color.MAGENTA
		ImageUtils.drawCenteredStringOutlined(graphics, "melhor amigo/amiga", Rectangle(0, 140, 400, 150), font);

		context.reply(
				template,
				"#loritta"
		).queue()
	}
}