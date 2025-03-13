package nl.eetgeenappels.homes

import nl.eetgeenappels.PREFIX
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to run this command!")
            return true
        }

        // check if sender has persmissions
        // check for args
        sender.sendMessage("${args.size}")
        if (args.isEmpty()) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You need to specify which home to teleport to!")
            return true
        }

        // find the home
        val homeName = args[0]

        val home = Homes.getHome(sender.uniqueId, homeName)
        if (home == null) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}Home doesn't exist!")
            return true
        }

        sender.sendMessage("$PREFIX ${ChatColor.GREEN}Teleporting you to home: ${ChatColor.BLUE}$homeName")

        // get the world
        val world = Bukkit.getWorld(home.world)

        val location = Location(world, home.positionX, home.positionY, home.positionZ)

        sender.teleport(location)


        return true
    }
}