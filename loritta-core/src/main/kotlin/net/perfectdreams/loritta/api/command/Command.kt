package net.perfectdreams.loritta.api.command

open class Command(val label: String, val aliases: Array<String> = arrayOf(), val category: CommandCategory = CommandCategory.MISCELLANEOUS) {
}