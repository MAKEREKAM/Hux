package kr.vanilage.main

import io.github.monun.kommand.kommand
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

var pluginInstance : JavaPlugin? = null

class Main : JavaPlugin() {
    companion object {
        var RED_CORE_HP = 200
        var BLUE_CORE_HP = 200

        val playerTeam : HashMap<UUID, String> = HashMap()
    }

    override fun onEnable() {
        pluginInstance = this
        Bukkit.getConsoleSender().sendMessage("Hello, World!")

        Bukkit.getPluginManager().registerEvents(EventListener(), this)
        Bukkit.getPluginManager().registerEvents(Missile(), this)

        kommand {
            register("craft") {
                requires { isPlayer }
                executes {
                    player.openWorkbench(null, true)
                }
            }

            register("gamestart") {
                requires { isPlayer && isOp && Bukkit.getOnlinePlayers().size == 4}
                executes {
                    var count = 0
                    for (i in Bukkit.getOnlinePlayers()) {
                        if (count < 2) {
                            playerTeam[i.uniqueId] = "RED"
                            i.teleport(Data.RED_SPAWN)
                        }

                        else {
                            playerTeam[i.uniqueId] = "BLUE"
                            i.teleport(Data.BLUE_SPAWN)
                        }

                        count++
                    }
                }
            }
        }

        val stoneGenerator = Data.itemStackGenerator(Material.STONE_BRICKS, "§a돌 생성기")
        val stoneCutter = Data.itemStackGenerator(Material.STONECUTTER, "§a돌 가공기")
        val ironGenerator = Data.itemStackGenerator(Material.IRON_BLOCK, "§a철 생성기")
        val furnace = Data.itemStackGenerator(Material.BLAST_FURNACE, "§a제련기")
        val goldGenerator = Data.itemStackGenerator(Material.GOLD_BLOCK, "§a금 생성기")
        val missile = Data.itemStackGenerator(Material.CREEPER_HEAD, "§a미사일")
        val powerMissile = Data.itemStackGenerator(Material.PLAYER_HEAD, "§a강력한 미사일")

        val stoneGeneratorRecipe = ShapedRecipe(NamespacedKey(this, "stone_generator"), stoneGenerator)
        stoneGeneratorRecipe.shape("OOO", "OWO", "OOO")
        stoneGeneratorRecipe.setIngredient('O', Material.OAK_LOG)
        stoneGeneratorRecipe.setIngredient('W', Material.WOODEN_PICKAXE)

        val stoneCutterRecipe = ShapedRecipe(NamespacedKey(this, "stone_cutter"), stoneCutter)
        stoneCutterRecipe.shape("SSS", "SSS", "SSS")
        stoneCutterRecipe.setIngredient('S', Material.STONE)

        val ironGeneratorRecipe = ShapedRecipe(NamespacedKey(this, "iron_generator"), ironGenerator)
        ironGeneratorRecipe.shape("CCC", "CSC", "CCC")
        ironGeneratorRecipe.setIngredient('C', Material.COBBLESTONE)
        ironGeneratorRecipe.setIngredient('S', Material.STONE_PICKAXE)

        val furnaceRecipe = ShapedRecipe(NamespacedKey(this, "furnace"), furnace)
        furnaceRecipe.shape("OOO", "OFO", "OOO")
        furnaceRecipe.setIngredient('O', Material.OAK_LOG)
        furnaceRecipe.setIngredient('F', Material.FURNACE)

        val goldGeneratorRecipe = ShapedRecipe(NamespacedKey(this, "gold_generator"), goldGenerator)
        goldGeneratorRecipe.shape("III", "IPI", "III")
        goldGeneratorRecipe.setIngredient('I', Material.IRON_INGOT)
        goldGeneratorRecipe.setIngredient('P', Material.IRON_PICKAXE)

        val missileRecipe = ShapedRecipe(NamespacedKey(this, "missile"), missile)
        missileRecipe.shape("III", "III", "III")
        missileRecipe.setIngredient('I', Material.IRON_BLOCK)

        val powerMissileRecipe = ShapedRecipe(NamespacedKey(this, "power_missile"), powerMissile)
        powerMissileRecipe.shape("GGG", "GMG", "GGG")
        powerMissileRecipe.setIngredient('G', Material.GOLD_BLOCK)
        powerMissileRecipe.setIngredient('M', missile)

        Bukkit.addRecipe(stoneGeneratorRecipe)
        Bukkit.addRecipe(stoneCutterRecipe)
        Bukkit.addRecipe(ironGeneratorRecipe)
        Bukkit.addRecipe(furnaceRecipe)
        Bukkit.addRecipe(goldGeneratorRecipe)
        Bukkit.addRecipe(missileRecipe)
        Bukkit.addRecipe(powerMissileRecipe)
    }
}