package net.perfectdreams.loritta.utils.extensions

import java.lang.reflect.Method
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

suspend fun Method.invokeSuspend(obj: Any, vararg args: Any?): Any? =
		suspendCoroutineUninterceptedOrReturn { cont ->
			try {
				for (arg in args) {
					println(arg)
				}
				invoke(obj, *args, cont)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}