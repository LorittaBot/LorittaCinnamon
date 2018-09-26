package net.perfectdreams.loritta

import net.perfectdreams.loritta.api.command.CommandManager
import net.perfectdreams.loritta.platform.PlatformType
import java.util.concurrent.ForkJoinPool

open class Loritta(val platformType: PlatformType) {
	companion object {
		lateinit var INSTANCE: Loritta
	}

	lateinit var commandManager: CommandManager
	val callbackPool = ForkJoinPool.commonPool()

	init {
		INSTANCE = this
	}
}