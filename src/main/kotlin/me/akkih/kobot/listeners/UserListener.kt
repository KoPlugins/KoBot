package me.akkih.kobot.listeners

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import me.akkih.kobot.KoBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class UserListener(bot: JDA) {

    init {
        bot.listener<GuildMemberJoinEvent> {
            it.user.openPrivateChannel().queue({ channel ->
                channel.sendMessageEmbeds(Embed {
                    title = "Welcome to KoPlugins Discord! :tada:"
                    description = """
                        You have joined the support community for KoPlugins.
                        
                        Feel free to ask anything, open help threads, make suggestions or report bugs!
                        
                        In this server, you'll recieve all updates from KoPlugins and will be able to test them as soon as they release! Isn't that cool?
                        
                        Also access our GitHub page @ https://github.com/KoPlugins
                    """.trimIndent()
                    color = KoBot.EMBED_COLOR
                }).queue()
            }, ErrorHandler().ignore(ErrorResponse.CANNOT_SEND_TO_USER))
        }
    }

}