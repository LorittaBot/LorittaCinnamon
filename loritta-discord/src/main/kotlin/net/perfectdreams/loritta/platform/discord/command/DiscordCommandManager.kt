package net.perfectdreams.loritta.platform.discord.command

import net.perfectdreams.loritta.api.command.CommandManager
import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.platform.discord.entity.DiscordMessage
import net.perfectdreams.loritta.platform.discord.entity.DiscordUser

class DiscordCommandManager : CommandManager() {
	override fun process(user: User, message: Message): Boolean {
		val content = message.content
		println(content)

		val args = content.split(" ").toMutableList()
		args.removeAt(0)
		val arg0 = args[0]
		args.removeAt(0)

		for (command in commands) {
			val labels = mutableListOf(command.label)
			labels.addAll(command.aliases)

			val valid = labels.any { arg0 == it }

			if (!valid)
				continue

			println("Do it!!! ${command::class.simpleName}")
			run(command, DiscordCommandContext(user as DiscordUser, message as DiscordMessage, args.toTypedArray()))
			return true
		}

		return false
	}
}