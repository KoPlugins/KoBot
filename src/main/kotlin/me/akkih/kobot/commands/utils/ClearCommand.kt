package me.akkih.kobot.commands.utils

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.commands.restrict
import dev.minn.jda.ktx.interactions.commands.upsertCommand
import dev.minn.jda.ktx.messages.Embed
import me.akkih.kobot.KoBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild

class ClearCommand(guild: Guild, bot: JDA) {

    init {
        guild.upsertCommand("clear", "Deletes the specified amount of messages") {
            restrict(guild = true, perm = Permission.MESSAGE_MANAGE)
            option<Int>("amount", "The amount of messages to delete", required = true)

            bot.onCommand("clear") {
                it.deferReply(true).queue()

                val amount = it.getOption("amount")!!.asInt

                if (amount < 1 || amount > 100 || amount == 0) {
                    it.hook.sendMessageEmbeds(Embed {
                        title = "Oops!"
                        description = """
                            Something went wrong.
                            Try using a number between 1 and 100, please!
                        """.trimIndent()
                        color = KoBot.EMBED_COLOR
                    }).queue()

                    return@onCommand
                }

                val messages = it.messageChannel.history.retrievePast(amount).complete()

                it.messageChannel.purgeMessages(messages)
                it.hook.sendMessageEmbeds(Embed {
                    title = "Success!"
                    description = """
                        Deleted $amount messages in this channel.
                    """.trimIndent()
                    color = KoBot.EMBED_COLOR
                }).queue()
            }
        }.queue()
    }

}