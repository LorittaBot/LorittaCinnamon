package net.perfectdreams.loritta.api.command

import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.api.network.RequestAction
import java.awt.image.BufferedImage

abstract class CommandContext(val user: User, val message: Message, val args: Array<String>) {
	abstract fun reply(content: String): RequestAction<Message>

	abstract fun reply(image: BufferedImage, content: String): RequestAction<Message>

	abstract fun getUserAt(idx: Int): User?
	abstract fun getImageAt(idx: Int): BufferedImage?
}