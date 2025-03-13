package nl.eetgeenappels.homes

import nl.eetgeenappels.PREFIX
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DeleteHomeCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You must be a player to run this command!")

            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("$PREFIX ${ChatColor.RED} You need to specify what home to delete")
            return true
        }

        val uuid = sender.uniqueId
        val homeName = args[0]

        if (!Homes.homeExists(uuid, homeName)) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You cannot delete a home that doesn't exist!")
            return true
        }

        sender.sendMessage("$PREFIX ${ChatColor.RED}Deleting home: \"$homeName\"")

        Homes.deleteHome(uuid, homeName)
        Homes.saveHomes()

        return true
    }
}