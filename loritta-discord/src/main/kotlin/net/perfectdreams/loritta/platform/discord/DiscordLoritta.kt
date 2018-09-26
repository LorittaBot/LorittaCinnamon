package net.perfectdreams.loritta.platform.discord

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.perfectdreams.loritta.Loritta
import net.perfectdreams.loritta.commands.images.*
import net.perfectdreams.loritta.commands.misc.PingCommand
import net.perfectdreams.loritta.platform.PlatformType
import net.perfectdreams.loritta.platform.discord.command.DiscordCommandManager
import net.perfectdreams.loritta.platform.discord.listeners.MessageListener
import java.io.File
import kotlin.concurrent.thread

class DiscordLoritta : Loritta(PlatformType.DISCORD) {
	val jda: JDA

	init {
		val token = File("discord_token.txt").readText()

		commandManager = DiscordCommandManager()

		commandManager.register(PingCommand())
		commandManager.register(RomeroBrittoCommand())
		commandManager.register(SAMCommand())
		commandManager.register(AmizadeCommand())
		commandManager.register(AtaCommand())
		commandManager.register(ContentAwareScaleCommand())

		jda = JDABuilder(token).build()
		jda.addEventListener(MessageListener(this))

		thread {
			while (true) {
				Thread.sleep(2500)
			}
		}
	}
}