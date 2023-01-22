package me.akkih.kobot.util

import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.messages.Embed
import io.github.cdimascio.dotenv.dotenv
import me.akkih.kobot.KoBot
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.requests.GatewayIntent

class GetStartedMessageSender {

    private val bot = light(dotenv().get("DISCORD_TOKEN")) {
        enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
    }
    private val guild: Guild
    private val getStartedChannel: TextChannel
    private val forumsChannels: String

    init {
        println("Logging in & updating variables...")

        bot.awaitReady()
        guild = bot.getGuildById(dotenv().get("GUILD"))!!
        getStartedChannel = guild.getTextChannelById(dotenv().get("GET_STARTED_CHANNEL_ID"))!!
        forumsChannels = "${guild.getForumChannelById(dotenv().get("HELP_CHANNEL_ID"))!!.asMention} and ${guild.getForumChannelById(dotenv().get("BUG_REPORT_CHANNEL_ID"))!!.asMention}"

        println("Bot ready! Sending messages...")
        getStartedChannel.sendMessageEmbeds(Embed {
            title = "Welcome to the server! :slight_smile:"
            description = """
                This is where you can get support (or give support!) to people that use KoPlugins.
                
                Here, we are a simple community ran by the devs to make sure everyone can answer questions and have a good experience using our plugins!
                
                We don't really have rules: we just want you to be nice and use common sense!
                
                If you have any questions, want to report a bug or have a suggestion, head over to $forumsChannels!
            """.trimIndent()
            color = KoBot.EMBED_COLOR
        }).complete().addReaction(Emoji.fromUnicode("✅")).queue()
    }

}