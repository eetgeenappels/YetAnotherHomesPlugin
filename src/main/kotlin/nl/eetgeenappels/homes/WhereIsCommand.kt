package nl.eetgeenappels.homes

import nl.eetgeenappels.PREFIX
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.roundToInt

class WhereIsCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You must be a Player to run this command!")
            return true
        }
        val uuid = sender.uniqueId

        if (!Homes.hasHomes(uuid)) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You are homeless!")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}You need to specify a home!")
            return true
        }

        if (!Homes.homeExists(uuid, args[0])) {
            sender.sendMessage("$PREFIX ${ChatColor.RED}This home doesn't exist!")
            return true
        }

        val home = Homes.getHome(uuid, args[0])!!

        sender.sendMessage("$PREFIX ${ChatColor.GREEN}Your home \"${home.name}\" is located at: ${ChatColor.BLUE} World: ${home.world} X: ${home.positionX.roundToInt()} Y: ${home.positionY.roundToInt()} Z: ${home.positionZ.roundToInt()}")

        return true
    }
}