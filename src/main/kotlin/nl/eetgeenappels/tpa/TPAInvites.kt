package nl.eetgeenappels.tpa

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import nl.eetgeenappels.PREFIX
import nl.eetgeenappels.homes.Homes
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit


object TPAInvites {

    private val invites: MutableList<Invite> = mutableListOf()
    private val INVITE_EXPIRATION_TIME = TimeUnit.MINUTES.toNanos(2) // 2 minutes expiration

    fun inviteTPA(tpaInvite: TPAInvite) {

        invites.add(tpaInvite)

        tpaInvite.invitePlayer.sendMessage("$PREFIX ${ChatColor.YELLOW}You have been sent a TPA invite by ${tpaInvite.sourcePlayer.displayName}!")
        val message = TextComponent("$${ChatColor.GREEN}Click here to accept")
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept ${tpaInvite.invitePlayer.displayName}")

        tpaInvite.invitePlayer.spigot().sendMessage(message)

    }
    fun inviteTPAHere(tpaHereInvite: TPAHereInvite) {

        invites.add(tpaHereInvite)

        tpaHereInvite.invitePlayer.sendMessage("$PREFIX ${ChatColor.YELLOW}You have been sent a TPA Here invite by ${tpaHereInvite.sourcePlayer.displayName}!")
        val message = TextComponent("$${ChatColor.GREEN}Click here to accept")
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept ${tpaHereInvite.invitePlayer.displayName}")

        tpaHereInvite.invitePlayer.spigot().sendMessage(message)
    }

    fun inviteHome(homeInvite: TPAHomeInvite) {
        invites.add(homeInvite)

        homeInvite.invitePlayer.sendMessage("$PREFIX ${ChatColor.YELLOW}You have been sent a Home to the home \"\"invite by ${homeInvite.sourcePlayer.displayName}!")
        val message = TextComponent("$${ChatColor.GREEN}Click here to accept")
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept ${homeInvite.invitePlayer.displayName}")

        homeInvite.invitePlayer.spigot().sendMessage(message)
    }

    fun getMostRecentInviteTo(invitePlayer: Player): Invite? {
        return invites.sortedByDescending { it.creationTime }.firstOrNull {invitePlayer == it.invitePlayer}
    }

    fun hasOutGoingInvite(sourcePlayer: Player): Boolean {
        return invites.none { sourcePlayer == it.sourcePlayer }
    }

    private fun getMostRecentInviteFrom(invitePlayer: Player, sourcePlayer: Player): Invite? {
        return invites
            .filter {
                invitePlayer.uniqueId == it.invitePlayer.uniqueId &&
                        sourcePlayer.uniqueId == it.sourcePlayer.uniqueId
            }
            .maxByOrNull { it.creationTime }
    }

    fun onTPAccept(invitePlayer: Player, sourcePlayer: Player) {

        invitePlayer.sendMessage("aaaaaaamogus")

        val invite = getMostRecentInviteFrom(invitePlayer, sourcePlayer) ?: return

        invitePlayer.sendMessage("sus")

        when (invite) {
            is TPAInvite -> {
                sourcePlayer.sendMessage("$PREFIX ${ChatColor.GREEN}${sourcePlayer.displayName} has accepted your TPA request!")
                invitePlayer.sendMessage("$PREFIX ${ChatColor.GREEN}You have accepted ${sourcePlayer.displayName}'s TPA request! You will now be teleported!")
            }
            is TPAHereInvite -> {
                sourcePlayer.sendMessage("$PREFIX ${ChatColor.GREEN}${sourcePlayer.displayName} has accepted your TPAHere request!")
                invitePlayer.sendMessage("$PREFIX ${ChatColor.GREEN}You have accepted ${sourcePlayer.displayName}'s TPAHere request! You will now be teleported!")
            }
            is TPAHomeInvite -> {
                sourcePlayer.sendMessage("$PREFIX ${ChatColor.GREEN}${sourcePlayer.displayName} has accepted your TPAHome request!")
                invitePlayer.sendMessage("$PREFIX ${ChatColor.GREEN}You have accepted ${sourcePlayer.displayName}'s TPAHome request! You will now be teleported to home: \"${invite.homeName}\"")
            }
        }

        teleportTPA(invite)

        invites.remove(invite)
    }

    private fun teleportTPA(invite: Invite) {

        val location: Location = invite.getTeleportLocation()
        val teleportingPlayer: Player = invite.teleportingPlayer()

        teleportingPlayer.teleport(location)
    }

    fun onTick() {
        val currentTime = System.nanoTime()
        invites.removeIf { currentTime - it.creationTime > INVITE_EXPIRATION_TIME }
    }

    abstract class Invite(val invitePlayer: Player, val sourcePlayer: Player) {
        val creationTime: Long = System.nanoTime()
        abstract fun getTeleportLocation(): Location
        abstract fun teleportingPlayer(): Player
    }

    class TPAInvite(invitePlayer: Player, sourcePlayer: Player) : Invite(invitePlayer, sourcePlayer) {
        override fun getTeleportLocation(): Location {
            return invitePlayer.location.clone()
        }

        override fun teleportingPlayer(): Player {
            return sourcePlayer
        }
    }
    class TPAHereInvite(invitePlayer: Player, sourcePlayer: Player) : Invite(invitePlayer, sourcePlayer) {
        override fun getTeleportLocation(): Location {
            return sourcePlayer.location
        }

        override fun teleportingPlayer(): Player {
            return invitePlayer
        }
    }

    class TPAHomeInvite(invitePlayer: Player, sourcePlayer: Player, val homeName: String) : Invite(invitePlayer, sourcePlayer) {
        override fun getTeleportLocation(): Location {
            val home = Homes.getHome(sourcePlayer.uniqueId, homeName)!!
            return Homes.getHomeLocation(home)
        }

        override fun teleportingPlayer(): Player {
            return invitePlayer
        }
    }
}
