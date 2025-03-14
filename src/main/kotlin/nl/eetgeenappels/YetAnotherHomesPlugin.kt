package nl.eetgeenappels

import nl.eetgeenappels.homes.*
import nl.eetgeenappels.tpa.TPACommand
import nl.eetgeenappels.tpa.TPAInvites
import nl.eetgeenappels.tpa.TPAcceptCommand
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

var PREFIX = ""
class YetAnotherHomesPlugin: JavaPlugin() {

    override fun onEnable() {
        instance = this
        PREFIX = "${ChatColor.DARK_GRAY}[${ChatColor.DARK_RED}Yet${ChatColor.RED}Another${ChatColor.GRAY}Homes${ChatColor.WHITE}Plugin${ChatColor.DARK_GRAY}]${ChatColor.WHITE}"
        setupCommands()
        Homes.loadHomes()
        startPeriodicTasks()
    }

    private fun setupCommands() {
        listOf(
            "home" to HomeCommand(),
            "homes" to HomesCommand(),
            "sethome" to SetHomeCommand(),
            "delhome" to DeleteHomeCommand(),
            "whereis" to WhereIsCommand(),
            "tpa" to TPACommand(),
            "tpaccept" to TPAcceptCommand()
        ).forEach{ (name, executor) ->
            getCommand(name)?.executor = executor
        }
        listOf(
            "home",
            "delhome",
            "whereis"
        ).forEach{ name ->
            getCommand(name)?.tabCompleter = HomesTabAutoCompleter()
        }
    }

    private fun startPeriodicTasks() {
        object : BukkitRunnable() {
            override fun run() {
                onTick()
            }
        }.runTaskTimer(this as Plugin, 0L, 1L)
    }

    private fun onTick() {
        TPAInvites.onTick()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {

    }

    companion object {
        lateinit var instance: YetAnotherHomesPlugin
        fun get(): YetAnotherHomesPlugin {
            return instance
        }
    }
}