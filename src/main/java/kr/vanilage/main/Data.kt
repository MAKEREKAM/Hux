package kr.vanilage.main

import io.github.monun.invfx.InvFX
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Data {
    companion object {
        val RED_SPAWN = Location(Bukkit.getWorld("world"), 25.0, 94.0, 0.0, 90F, 0F)
        val BLUE_SPAWN = Location(Bukkit.getWorld("world"), -25.0, 94.0, 0.0, -90F, 0F)
        val RED_CORE = Location(Bukkit.getWorld("world"), 34.0, 107.0, 0.0)
        val BLUE_CORE = Location(Bukkit.getWorld("world"), -34.0, 107.0, 0.0)

        val STONE_CUTTER = InvFX.frame(1, Component.text("§c돌 가공기")) {
            slot(4, 0) {
                item = itemStackGenerator(Material.COBBLESTONE, "§a돌 가공기 작동")

                onClick { e ->
                    val p = e.whoClicked as Player

                    for (i in p.inventory.contents) {
                        if (i?.type!! == Material.STONE) {
                            i.amount--
                            p.inventory.addItem(ItemStack(Material.COBBLESTONE))
                            p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
                        }
                    }
                }
            }
        }

        val FURNACE = InvFX.frame(1, Component.text("§c제련기")) {
            slot(4, 0) {
                item = itemStackGenerator(Material.NETHERITE_INGOT, "§a제련기 작동")

                onClick { e ->
                    val p = e.whoClicked as Player

                    if (e.isLeftClick) {
                        for (i in p.inventory.contents) {
                            if (i?.type!! == Material.IRON_ORE) {
                                i.amount--
                                p.inventory.addItem(ItemStack(Material.IRON_INGOT))
                                p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
                            }
                        }
                    }

                    if (e.isRightClick) {
                        for (i in p.inventory.contents) {
                            if (i?.type!! == Material.GOLD_ORE) {
                                i.amount--
                                p.inventory.addItem(ItemStack(Material.GOLD_INGOT))
                                p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
                            }
                        }
                    }
                }
            }
        }

        fun itemStackGenerator(type : Material, name : String) : ItemStack {
            val stack = ItemStack(type)
            val meta = stack.itemMeta
            meta.setDisplayName(name)

            stack.itemMeta = meta
            return stack
        }
    }
}