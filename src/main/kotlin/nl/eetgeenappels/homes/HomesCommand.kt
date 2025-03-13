package nl.eetgeenappels.homes

import nl.eetgeenappels.PREFIX
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomesCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You must be a Player to run this command!")
            return true
        }

        val uuid = sender.uniqueId
        val homes = Homes.playerMap[uuid]

        if (homes == null) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You are homeless!")
            return true
        }

        sender.sendMessage("$PREFIX ${ChatColor.BLUE}Your homes are:")

        homes.forEach {
            sender.sendMessage("${ChatColor.GRAY}- ${it.name}")
        }

        return true
    }
}