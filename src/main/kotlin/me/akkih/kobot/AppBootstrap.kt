package me.akkih.kobot

import me.akkih.kobot.util.GetStartedMessageSender
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println(args.joinToString(", "))
    if (args.isNotEmpty() && args[0] == "sendGetStartedMessage") {
        GetStartedMessageSender()
        exitProcess(0)
    }
    KoBot()
}