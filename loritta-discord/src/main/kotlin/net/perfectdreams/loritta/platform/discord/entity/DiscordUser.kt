package net.perfectdreams.loritta.platform.discord.entity

import net.perfectdreams.loritta.api.entity.User

class DiscordUser(val handle: net.dv8tion.jda.core.entities.User) : User {
	override val name: String
		get() = handle.name
	override val avatarUrl: String
		get() = handle.effectiveAvatarUrl
}