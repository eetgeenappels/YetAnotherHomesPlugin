package nl.eetgeenappels.homes

import nl.eetgeenappels.PREFIX
import nl.eetgeenappels.YetAnotherHomesPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.nio.DoubleBuffer
import java.util.*

object Homes {

    val playerMap: MutableMap<UUID, MutableList<Home>> = mutableMapOf()
    val usermapFile = File(YetAnotherHomesPlugin.get().dataFolder, "usermap.yml")
    var usermapConfig: YamlConfiguration? = null

    fun getHome(uuid: UUID, homeName: String): Home? {
        return playerMap[uuid]?.firstOrNull { it.name == homeName }
    }

    fun homeExists(uuid: UUID, homeName: String): Boolean {
        return playerMap[uuid]?.any { it.name == homeName } ?: false
    }

    fun createHome(uuid: UUID, location: Location, homeName: String) {
        if (playerMap[uuid] == null) {
            playerMap[uuid] = mutableListOf()
        }
        val home = Home(homeName, location.world.name, location.x, location.y, location.z)
        playerMap[uuid]?.add(home)
    }

    fun deleteHome(uuid: UUID, name: String) {
        if (!homeExists(uuid, name))
            return
        playerMap[uuid]?.remove(playerMap[uuid]?.first { it.name == name })
    }

    fun getHomeLocation(home: Home): Location {
        return Location(Bukkit.getWorld(home.world), home.positionX, home.positionY, home.positionZ)
    }

    fun hasHomes(uuid: UUID): Boolean {
        if (!playerMap.contains(uuid)) return false
        if (playerMap[uuid]?.isEmpty() == true) return false
        return true
    }

    fun saveHomes() {

        // create usermap file
        if (!usermapFile.exists()) {
            usermapFile.parentFile.mkdirs()
            usermapFile.createNewFile()
        }

        usermapConfig = YamlConfiguration()

        for (uuid in playerMap.keys) {

            if (!hasHomes(uuid))
                continue

            val homes = playerMap[uuid]!!

            for (home in homes) {
                val positionX = home.positionX
                val positionY = home.positionY
                val positionZ = home.positionZ
                val world = home.world
                val homeName = home.name

                usermapConfig?.set("homes.$uuid.$homeName.positionX", positionX)
                usermapConfig?.set("homes.$uuid.$homeName.positionY", positionY)
                usermapConfig?.set("homes.$uuid.$homeName.positionZ", positionZ)
                usermapConfig?.set("homes.$uuid.$homeName.world", world)
            }

        }

        usermapConfig?.save(usermapFile)
    }

    fun loadHomes() {
        if (!usermapFile.exists()) return

        usermapConfig = YamlConfiguration.loadConfiguration(usermapFile)

        for (uuidString in usermapConfig?.getConfigurationSection("homes")?.getKeys(false) ?: emptySet()) {
            val uuid = UUID.fromString(uuidString)
            val homesList = mutableListOf<Home>()

            for (homeName in usermapConfig?.getConfigurationSection("homes.$uuidString")?.getKeys(false) ?: emptySet()) {
                val positionX = usermapConfig?.getDouble("homes.$uuidString.$homeName.positionX") ?: 0.0
                val positionY = usermapConfig?.getDouble("homes.$uuidString.$homeName.positionY") ?: 100.0
                val positionZ = usermapConfig?.getDouble("homes.$uuidString.$homeName.positionZ") ?: 0.0
                val world = usermapConfig?.getString("homes.$uuidString.$homeName.world") ?: "world"

                val home = Home(homeName, world, positionX, positionY, positionZ)
                homesList.add(home)
            }

            playerMap[uuid] = homesList
        }
    }

    fun getHomeNames(uuid: UUID): MutableList<String> {
        if (!hasHomes(uuid))
            return mutableListOf()

        val homeNames: MutableList<String> = mutableListOf()
        playerMap[uuid]?.forEach {
            homeNames.add(it.name)
        }

        return homeNames
    }

    fun teleportToHome(player: Player, home: Home) {
        player.sendMessage("$PREFIX ${ChatColor.GREEN}Teleporting you to home: ${ChatColor.BLUE}${home.name}")

        // get the world
        val world = Bukkit.getWorld(home.world)

        val location = Location(world, home.positionX, home.positionY, home.positionZ)

        player.teleport(location)
    }

}