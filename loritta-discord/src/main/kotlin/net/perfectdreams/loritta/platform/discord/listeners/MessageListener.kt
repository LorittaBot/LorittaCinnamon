package net.perfectdreams.loritta.platform.discord.listeners

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import net.perfectdreams.loritta.platform.discord.DiscordLoritta
import net.perfectdreams.loritta.platform.discord.entity.DiscordMessage
import net.perfectdreams.loritta.platform.discord.entity.DiscordUser

class MessageListener(val loritta: DiscordLoritta) : ListenerAdapter() {
	override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
		println(event.message.contentRaw)
		if (event.message.contentRaw.replace("!", "").startsWith("<@494237647678734356>")) {
			loritta.commandManager.process(
					DiscordUser(event.author),
					DiscordMessage(event.message)
			)
		}
	}
}