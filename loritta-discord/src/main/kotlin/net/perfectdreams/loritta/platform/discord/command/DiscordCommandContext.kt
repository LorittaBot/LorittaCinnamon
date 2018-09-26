package net.perfectdreams.loritta.platform.discord.command

import net.dv8tion.jda.core.MessageBuilder
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.api.network.RequestAction
import net.perfectdreams.loritta.platform.discord.entity.DiscordMessage
import net.perfectdreams.loritta.platform.discord.entity.DiscordUser
import net.perfectdreams.loritta.utils.LorittaUtils
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class DiscordCommandContext(val userHandle: DiscordUser, val messageHandle: DiscordMessage, args: Array<String>) : CommandContext(userHandle, messageHandle, args) {
	override fun reply(content: String): RequestAction<Message> {
		return RequestAction {
			val message = messageHandle.handle.channel.sendMessage(content).complete()
			return@RequestAction DiscordMessage(message)
		}
	}

	override fun reply(image: BufferedImage, content: String): RequestAction<Message> {
		return RequestAction {
			val os = ByteArrayOutputStream()
			ImageIO.write(image, "png", os)

			val inputStream = ByteArrayInputStream(os.toByteArray())
			val message = messageHandle.handle.channel.sendFile(inputStream, "image.png", MessageBuilder().setContent(content).build()).complete()

			return@RequestAction DiscordMessage(message)
		}
	}

	override fun getUserAt(idx: Int): User? {
		if (this.args.size > idx) { // Primeiro iremos verificar se existe uma imagem no argumento especificado
			val link = this.args[idx] // Ok, será que isto é uma URL?

			// Vamos verificar por menções, uma menção do Discord é + ou - assim: <@123170274651668480>
			for (user in this.messageHandle.handle.mentionedUsers) {
				if (user != null && user.asMention == link.replace("!", "")) { // O replace é necessário já que usuários com nick tem ! no mention (?)
					// Diferente de null? Então vamos usar o avatar do usuário!
					return DiscordUser(user)
				}
			}
		}

		return null
	}

	override fun getImageAt(idx: Int): BufferedImage? {
		val user = getUserAt(idx) ?: return null

		return LorittaUtils.downloadImage(user.avatarUrl)
	}
}