package nl.eetgeenappels.tpa

import nl.eetgeenappels.PREFIX
import nl.eetgeenappels.homes.Homes
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

object TPAInvites {

    private val invites: MutableList<Invite> = mutableListOf()
    private val INVITE_EXPIRATION_TIME = TimeUnit.MINUTES.toNanos(2) // 2 minutes expiration

    fun invite(invite: Invite) {
        invites.add(invite)
        invite.invitePlayer.sendMessage("$PREFIX ${invite.sourcePlayer.name} has invited you to teleport.")
    }

    fun hasSentInvite(player: Player): Boolean {
        return invites.any { it.invitePlayer == player }
    }

    fun onTPAccept(player: Player, sourcePlayer: Player) {
        val invite = invites.sortedBy { it.creationTime }
            .find { it.invitePlayer == player && it.sourcePlayer == sourcePlayer }

        if (invite != null) {
            when (invite) {
                is TPAInvite -> {
                    sourcePlayer.teleport(player.location)
                    player.sendMessage("$PREFIX You have teleported to ${invite.sourcePlayer.name}.")
                }
                is TPAHereInvite -> {
                    player.teleport(invite.sourcePlayer.location)
                    player.sendMessage("$PREFIX You have teleported to ${invite.sourcePlayer.name}.")
                }
                is HomeInvite -> {
                    player.sendMessage("$PREFIX You have accepted ${invite.sourcePlayer.name}'s request to teleport to their home: ${invite.homeName}")
                    val home = Homes.getHome(invite.sourcePlayer.uniqueId, invite.homeName)!!
                    Homes.teleportToHome(player, home)
                }
            }
            invites.remove(invite)
            invite.sourcePlayer.sendMessage("$PREFIX ${player.name} accepted your teleport request.")
        } else {
            player.sendMessage("No pending teleport requests.")
        }
    }

    fun getMostRecentInviteTo(player: Player): Invite? {
        return invites.filter { it.invitePlayer == player }.maxByOrNull { it.creationTime }
    }

    fun onTick() {
        val currentTime = System.nanoTime()
        invites.removeIf { currentTime - it.creationTime > INVITE_EXPIRATION_TIME }
    }

    abstract class Invite(val invitePlayer: Player, val sourcePlayer: Player) {
        val creationTime: Long = System.nanoTime()
    }

    class TPAInvite(invitePlayer: Player, sourcePlayer: Player) : Invite(invitePlayer, sourcePlayer)
    class TPAHereInvite(invitePlayer: Player, sourcePlayer: Player) : Invite(invitePlayer, sourcePlayer)
    class HomeInvite(invitePlayer: Player, sourcePlayer: Player, val homeName: String) : Invite(invitePlayer, sourcePlayer)
}
