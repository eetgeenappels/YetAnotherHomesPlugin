package nl.eetgeenappels.tpa

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import nl.eetgeenappels.PREFIX
import nl.eetgeenappels.YetAnotherHomesPlugin
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class TPACommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You must be a player to use this command!")
            return true
        }
        if (args.isEmpty()) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}To what person do you want to teleport!")
            return true
        }
        if (sender.name == args[0]) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You can't teleport to yourself!")
            return true
        }
        val invitePlayer = YetAnotherHomesPlugin.get().server.onlinePlayers.firstOrNull {sender.name.lowercase() == it.name.lowercase()}

        if (invitePlayer == null) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}That player is not online right now!")
            return true
        }

        TPAInvites.invite(TPAInvites.TPAInvite(invitePlayer, sender))

        return true
    }
}