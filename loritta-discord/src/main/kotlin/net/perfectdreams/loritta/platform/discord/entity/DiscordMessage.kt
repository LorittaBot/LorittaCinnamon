package net.perfectdreams.loritta.platform.discord.entity

import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User

class DiscordMessage(val handle: net.dv8tion.jda.core.entities.Message) : Message {
	override val user: User
		get() = DiscordUser(handle.author)
	override val content: String
		get() = handle.contentRaw
}