package net.perfectdreams.loritta.utils

import net.perfectdreams.loritta.api.command.CommandContext
import java.io.File
import java.util.*

object Constants {
	const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0"
	val ASSETS_FOLDER by lazy {
		File("assets/")
	}
	val random = SplittableRandom()

	/**
	 * Used in conjuction with the elvis operation ("?:") plus a "return;" when the image is null, this allows the user to receive feedback if the image
	 * is valid or, if he doesn't provide any arguments to the command, explain how the command works.
	 *
	 * ```
	 * context.getImageAt(0) ?: run { Constants.INVALID_IMAGE_REPLY.invoke(context); return; }
	 * ```
	 */
	val INVALID_IMAGE_REPLY: suspend ((CommandContext) -> Unit) = { context ->
		if (context.args.isEmpty()) {
			// context.explain()
		} else {
			context.reply(
					"Eu não encontrei nenhuma imagem válida para eu usar! (Eu tento pegar imagens em links, upload de imagens, avatares de usuários mencionados, emojis... mas eu encontrei nada nessa sua mensagem!)"
			).queue()
		}
	}
}