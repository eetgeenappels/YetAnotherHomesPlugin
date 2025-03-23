package nl.eetgeenappels.tpa

import nl.eetgeenappels.PREFIX
import nl.eetgeenappels.YetAnotherHomesPlugin
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class TPAcceptCommand: CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            return true
        }

        if (args.isEmpty()) {
            // tpa accept most recent tpa request for this person
            val invite = TPAInvites.getMostRecentInviteTo(sender)

            // if no invites are present tell the player
            if (invite == null) {
                sender.sendMessage("$PREFIX ${ChatColor.RED}You don't have any invites right now!")
                return true
            }

            TPAInvites.onTPAccept(sender, invite.sourcePlayer)

        } else {
            // a player has been mentioned
            val sourcePlayerName = args[0]
            val sourcePlayer = YetAnotherHomesPlugin.get().server.getPlayer(sourcePlayerName)
            if (sourcePlayer == null) {
                sender.sendMessage("$PREFIX ${ChatColor.RED}That player isn't online!")
                return true
            }
            TPAInvites.onTPAccept(sender, sourcePlayer)
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String> {
        return mutableListOf()
    }
}