package me.akkih.kobot.commands.utils

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.restrict
import dev.minn.jda.ktx.interactions.commands.upsertCommand
import dev.minn.jda.ktx.messages.Embed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.akkih.kobot.KoBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import java.io.BufferedReader
import java.io.InputStreamReader

class VersionCommand(guild: Guild, bot: JDA) {

    init {
        guild.upsertCommand("version", "Check the version of the bot") {
            restrict(guild = false)

            bot.onCommand("version") {
                it.deferReply().queue()

                val process = withContext(Dispatchers.IO) {
                    ProcessBuilder("git", "rev-parse", "HEAD").start()
                }
                val version = BufferedReader(InputStreamReader(process.inputStream)).readLines()[0]

                it.hook.sendMessageEmbeds(Embed {
                    title = "Current version"
                    url = "https://github.com/KoPlugins/KoBot/commit/$version"
                    description = "The bot is currently running version `${version.take(7)}`."
                    color = KoBot.EMBED_COLOR
                }).queue()
            }
        }.queue()
    }

}