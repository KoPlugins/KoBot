package me.akkih.kobot

import dev.minn.jda.ktx.events.listener
import me.akkih.kobot.commands.*
import dev.minn.jda.ktx.jdabuilder.cache
import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.util.SLF4J
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.*
import me.akkih.kobot.commands.users.ProfileCommand
import me.akkih.kobot.commands.utils.ClearCommand
import me.akkih.kobot.commands.utils.VersionCommand
import me.akkih.kobot.listeners.UserListener
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import kotlin.time.Duration.Companion.seconds

@OptIn(DelicateCoroutinesApi::class)
class KoBot {

    private val activities = arrayOf(
        Activity.playing("using KoPlugins"),
        Activity.listening("Akkih crying over code"),
        Activity.watching("Akkih screaming over code"),
        Activity.watching("Penguin and Akkih marrying"),
        Activity.playing("VALORANT"),
    )

    private val bot = light(dotenv().get("DISCORD_TOKEN")) {
        enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
        cache += CacheFlag.FORUM_TAGS
        setMemberCachePolicy(MemberCachePolicy.ALL)
        setChunkingFilter(ChunkingFilter.ALL)
        setActivity(activities[0])
    }.also { logger.info("Logged into ${it.selfUser.name}#${it.selfUser.discriminator}") }
    private val guild: Guild

    private lateinit var activityJob: Job

    init {
        logger.info("Starting bot...")

        bot.awaitReady()
        guild = bot.getGuildById(dotenv().get("GUILD"))!!

        registerCommands()
        registerListeners()

        GlobalScope.launch {
            activityJob = async {
                while (true) {
                    for (activity in activities) {
                        bot.presence.activity = activity
                        delay(60.seconds.inWholeMilliseconds)
                    }
                }
            }
        }
    }

    private fun registerCommands() {
        logger.info("Registering commands...")
        bot.listener<GenericCommandInteractionEvent> {
            logger.info("${it.user.name}#${it.user.discriminator} executed ${it.commandString}")
        }

        ClearCommand(guild, bot)
        VersionCommand(guild, bot)
        ProfileCommand(guild, bot)
        logger.info("Commands registered successfully!")
    }

    private fun registerListeners() {
        logger.info("Registering listeners...")
        UserListener(bot)
        logger.info("Listeners registered successfully!")
    }

    companion object {
        const val EMBED_COLOR = 0x2F3136
        val logger: Logger by SLF4J

        val User.nameAndTag: String
            get() = "$name#$discriminator"
    }

}