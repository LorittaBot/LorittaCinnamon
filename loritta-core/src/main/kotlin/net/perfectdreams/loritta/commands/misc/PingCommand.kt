package net.perfectdreams.loritta.commands.misc

import net.perfectdreams.loritta.api.command.Command
import net.perfectdreams.loritta.api.command.CommandCategory
import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User

class PingCommand : Command("ping", category = CommandCategory.MISCELLANEOUS) {
	@Subcommand
	suspend fun root(user: User, message: Message, context: CommandContext) {
		println("owo")
		context.reply("Olá ${user.name} ^-^! Pong!!!").queue {
			println("Done!")
		}
	}
}