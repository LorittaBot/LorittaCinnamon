package net.perfectdreams.loritta.platform.twitter.command

import net.perfectdreams.loritta.api.command.CommandManager
import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.platform.twitter.entity.TwitterMessage
import net.perfectdreams.loritta.platform.twitter.entity.TwitterUser

class TwitterCommandManager : CommandManager() {
	override fun process(user: User, message: Message): Boolean {
		val content = message.content

		val args = content.split(" ").toMutableList()
		val arg0 = args[0]
		args.removeAt(0)

		for (command in commands) {
			val labels = mutableListOf(command.label)
			labels.addAll(command.aliases)

			val valid = labels.any { arg0 == it }

			if (!valid)
				continue

			run(command, TwitterCommandContext(user as TwitterUser, message as TwitterMessage, args.toTypedArray()))
			return true
		}

		return false
	}
}