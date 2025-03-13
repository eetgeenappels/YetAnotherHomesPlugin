package nl.eetgeenappels.homes

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class HomesTabAutoCompleter: TabCompleter {
    override fun onTabComplete(
        sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?
    ): MutableList<String> {
        if (sender !is Player) {
            return mutableListOf()
        }
        return Homes.getHomeNames(sender.uniqueId)
    }
}