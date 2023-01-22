package me.akkih.kobot.commands

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.commands.restrict
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import dev.minn.jda.ktx.interactions.commands.upsertCommand
import dev.minn.jda.ktx.messages.Embed
import me.akkih.kobot.KoBot
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member

class ProfileCommand(guild: Guild, bot: JDA) {

    private lateinit var member: Member

    init {
        guild.upsertCommand("profile", "View a user's profile") {
            restrict(guild = true, perm = Permission.MODERATE_MEMBERS)
            option<Member>("member", "Select member to view profile", required = false)

            bot.onCommand("profile") {
                it.deferReply().queue()

                member = if (it.getOption("member") != null) {
                    it.getOption("member")?.asMember!!
                } else { it.member!! }

                it.hook.sendMessageEmbeds(Embed {
                    title = "${member.user.name}'s Information"
                    description = """
                        Account created on: `${member.user.timeCreated.toLocalDate()}`
                        Joined server on: `${member.timeJoined.toLocalDate()}`
                    """.trimIndent()
                    color = KoBot.EMBED_COLOR
                    thumbnail = member.effectiveAvatarUrl
                }).queue()
            }
        }.queue()
    }

}