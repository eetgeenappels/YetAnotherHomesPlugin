package nl.eetgeenappels.homes

import nl.eetgeenappels.PREFIX
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetHomeCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You must be a Player to run this command!")
            return true
        }

        // check args
        if (args.isEmpty()) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You must make a name for your home!")
            return true
        }

        val uuid = sender.uniqueId
        val homeName = args[0]

        if (Homes.homeExists(uuid, homeName)) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}A home with this name already exists!")
            return true
        }
        Homes.createHome(uuid, sender.location, homeName)
        sender.sendMessage("$PREFIX ${ChatColor.GREEN}Successfully created home!")

        Homes.saveHomes()
        return true
    }
}