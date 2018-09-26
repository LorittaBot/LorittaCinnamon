package net.perfectdreams.loritta.api.network

import kotlinx.coroutines.runBlocking

class RequestAction<T>(val action: () -> T) {
	suspend fun queue(success: ((T) -> (Unit))? = null, failure: ((Throwable) -> (Unit))? = null) {
		runBlocking {
			try {
				val result = action.invoke()
				success?.invoke(result)
			} catch (e: Throwable) {
				failure?.invoke(e)
			}
		}
	}
}