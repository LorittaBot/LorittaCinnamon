package net.perfectdreams.loritta.api.command

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import net.perfectdreams.loritta.api.command.annotation.ArgumentType
import net.perfectdreams.loritta.api.command.annotation.InjectArgument
import net.perfectdreams.loritta.api.command.annotation.Subcommand
import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.utils.extensions.invokeSuspend
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.coroutines.Continuation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.kotlinFunction

abstract class CommandManager {
	val commands = mutableListOf<Command>()

	abstract fun process(user: User, message: Message): Boolean

	fun register(command: Command) {
		commands.add(command)
	}

	fun run(command: Command, context: CommandContext) {
		val args = context.args
		val baseClass = command::class.java

		// Ao executar, nós iremos pegar várias anotações para ver o que devemos fazer agora
		val methods = baseClass.methods

		for (method in methods.filter { it.isAnnotationPresent(Subcommand::class.java) }.sortedByDescending { it.parameterCount }) {
			val subcommandAnnotation = method.getAnnotation(Subcommand::class.java)
			val values = subcommandAnnotation.values
			for (value in values.map { it.split(" ") }) {
				var matchedCount = 0
				for ((index, text) in value.withIndex()) {
					val arg = args.getOrNull(index)
					if (text == arg)
						matchedCount++
				}
				val matched = matchedCount == value.size
				if (matched) {
					if (executeMethod(command, method, context, context.message, "+" /* TODO: Corrigir isto */, args ,matchedCount))
						return
				}
			}
		}

		// Nenhum comando foi executado... #chateado
		for (method in methods.filter { it.isAnnotationPresent(Subcommand::class.java) }.sortedByDescending { it.parameterCount }) {
			val subcommandAnnotation = method.getAnnotation(Subcommand::class.java)
			if (subcommandAnnotation.values.isEmpty()) {
				if (executeMethod(command, method, context, context.message, "+" /* TODO: Corrigir isto */, args, 0))
					return
			}
		}
		return
	}

	fun executeMethod(command: Command, method: Method, context: CommandContext, message: Message, commandLabel: String, args: Array<String>, skipArgs: Int): Boolean {
		// check method arguments
		val arguments = args.toMutableList()
		for (i in 0 until skipArgs)
			arguments.removeAt(0)

		val isMarkedSuspend = method.kotlinFunction!!.isSuspend
		val params = getContextualArgumentList(method, context, message.user, commandLabel, arguments)

		// Agora iremos "validar" o argument list antes de executar
		for ((index, parameter) in method.kotlinFunction!!.valueParameters.withIndex()) {
			if (!parameter.type.isMarkedNullable && params[index] == null)
				return false
		}

		val paramsSize = params.size
		var parameterCount = method.parameterCount
		if (isMarkedSuspend)
			parameterCount -= 1

		if (paramsSize != parameterCount)
			return false

		try {
			GlobalScope.async {
				method.invokeSuspend(command, *params.toTypedArray())
			}
		} catch (e: InvocationTargetException) {
			val targetException = e.targetException
			if (targetException is ExecutedCommandException) {
				// message.channel.sendMessage(e.message ?: "Algo de errado aconteceu ao usar o comando...").queue()
			} else {
				throw e
			}
		}
		return true
	}

	fun getContextualArgumentList(method: Method, context: CommandContext, sender: User, commandLabel: String, arguments: MutableList<String>): List<Any?> {
		var dynamicArgIdx = 0
		val params = mutableListOf<Any?>()

		parameterForEach@for ((index, param) in method.parameters.withIndex()) {
			val typeName = param.type.simpleName.toLowerCase()
			val injectArgumentAnnotation = param.getAnnotation(InjectArgument::class.java)
			when {
				injectArgumentAnnotation != null && injectArgumentAnnotation.type == ArgumentType.COMMAND_LABEL -> {
					params.add(commandLabel)
				}
				injectArgumentAnnotation != null && injectArgumentAnnotation.type == ArgumentType.ARGUMENT_LIST -> {
					params.add(arguments.joinToString(" "))
				}
				// Ao usar "suspend", uma Continuation é aplicada no final dos parâmetros, por isto iremos ignorar ela.
				param.type.isAssignableFrom(Continuation::class.java) && method.parameters.size == index + 1 -> {
					break@parameterForEach
				}
				param.type.isAssignableFrom(User::class.java) -> { params.add(sender) }
				param.type.isAssignableFrom(Message::class.java) -> { params.add(context.message) }
				param.type.isAssignableFrom(String::class.java) -> {
					params.add(arguments.getOrNull(dynamicArgIdx))
					dynamicArgIdx++
				}
				param.type.isAssignableFrom(CommandContext::class.java) -> {
					params.add(context)
				}
				// Sim, é necessário usar os nomes assim, já que podem ser tipos primitivos ou objetos
				typeName == "int" || typeName == "integer" -> {
					params.add(arguments.getOrNull(dynamicArgIdx)?.toIntOrNull())
					dynamicArgIdx++
				}
				typeName == "double" -> {
					params.add(arguments.getOrNull(dynamicArgIdx)?.toDoubleOrNull())
					dynamicArgIdx++
				}
				typeName == "float" -> {
					params.add(arguments.getOrNull(dynamicArgIdx)?.toFloatOrNull())
					dynamicArgIdx++
				}
				typeName == "long" -> {
					params.add(arguments.getOrNull(dynamicArgIdx)?.toLongOrNull())
					dynamicArgIdx++
				}
				param.type.isAssignableFrom(Array<String>::class.java) -> {
					params.add(arguments.subList(dynamicArgIdx, arguments.size).toTypedArray())
				}
				param.type.isAssignableFrom(Array<Int?>::class.java) -> {
					params.add(arguments.subList(dynamicArgIdx, arguments.size).map { it.toIntOrNull() }.toTypedArray())
				}
				param.type.isAssignableFrom(Array<Double?>::class.java) -> {
					params.add(arguments.subList(dynamicArgIdx, arguments.size).map { it.toDoubleOrNull() }.toTypedArray())
				}
				param.type.isAssignableFrom(Array<Float?>::class.java) -> {
					params.add(arguments.subList(dynamicArgIdx, arguments.size).map { it.toFloatOrNull() }.toTypedArray())
				}
				param.type.isAssignableFrom(Array<Long?>::class.java) -> {
					params.add(arguments.subList(dynamicArgIdx, arguments.size).map { it.toLongOrNull() }.toTypedArray())
				}
				else -> params.add(null)
			}
		}
		return params
	}
}