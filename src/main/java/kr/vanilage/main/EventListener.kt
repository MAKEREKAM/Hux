package kr.vanilage.main

import io.github.monun.invfx.openFrame
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack


class EventListener : Listener {
    @EventHandler
    fun onPlace(e : BlockPlaceEvent) {
        if (e.player.gameMode != GameMode.CREATIVE) e.isCancelled = true
    }

    @EventHandler
    fun onBreak(e : BlockBreakEvent) {
        if (e.player.gameMode != GameMode.CREATIVE) e.isCancelled = true
    }

    @EventHandler
    fun onExplosion(e : BlockExplodeEvent) {
        for (b in e.blockList()) {
            val state = b.state

            Bukkit.getScheduler().scheduleSyncDelayedTask(pluginInstance!!, Runnable {
                state.update(true, false)
            }, 0)
        }
    }

    @EventHandler
    fun onDrop(e : PlayerDropItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onInteract(e : PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return

        if (e.clickedBlock?.type == Material.STONECUTTER) {
            e.isCancelled = true
            e.player.openFrame(Data.STONE_CUTTER)
        }

        if (e.clickedBlock?.type == Material.BLAST_FURNACE) {
            e.isCancelled = true
            e.player.openFrame(Data.FURNACE)
        }
    }

    @EventHandler
    fun onRespawn(e : PlayerRespawnEvent) {
        if (Main.playerTeam[e.player.uniqueId]!! == "RED") {
            Bukkit.getScheduler().runTaskLater(pluginInstance!!, Runnable {
                e.player.teleport(Data.RED_SPAWN)
            }, 1)

        }

        else {
            Bukkit.getScheduler().runTaskLater(pluginInstance!!, Runnable {
                e.player.teleport(Data.BLUE_SPAWN)
            }, 1)
        }
    }

    @EventHandler
    fun onSwap(e : PlayerSwapHandItemsEvent) {
        e.isCancelled = true

        if (e.player.isSneaking) {
            e.player.openWorkbench(null, true)
            return
        }

        if (e.offHandItem?.itemMeta?.displayName == null) return

        if (e.offHandItem!!.itemMeta.displayName == "§a돌 생성기") {
            e.player.location.block.type = Material.STONE_BRICKS

            val dropLocation = e.player.location

            Bukkit.getScheduler().runTaskTimer(pluginInstance!!, Runnable {
                e.player.world.dropItem(dropLocation, ItemStack(Material.STONE))
                e.player.playSound(dropLocation, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
            }, 0, 60)

            if (e.player.gameMode != GameMode.CREATIVE) e.player.inventory.itemInMainHand.amount--;
        }

        if (e.offHandItem!!.itemMeta.displayName == "§a돌 가공기") {
            e.player.location.block.type = Material.STONECUTTER

            if (e.player.gameMode != GameMode.CREATIVE) e.player.inventory.itemInMainHand.amount--;
            e.player.playSound(e.player.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
        }

        if (e.offHandItem!!.itemMeta.displayName == "§a철 생성기") {
            e.player.location.block.type = Material.IRON_BLOCK

            val dropLocation = e.player.location

            Bukkit.getScheduler().runTaskTimer(pluginInstance!!, Runnable {
                e.player.world.dropItemNaturally(dropLocation, ItemStack(Material.IRON_ORE))
                e.player.playSound(dropLocation, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
            }, 0, 60)

            if (e.player.gameMode != GameMode.CREATIVE) e.player.inventory.itemInMainHand.amount--;
        }

        if (e.offHandItem!!.itemMeta.displayName == "§a제련기") {
            e.player.location.block.type = Material.BLAST_FURNACE

            if (e.player.gameMode != GameMode.CREATIVE) e.player.inventory.itemInMainHand.amount--;
            e.player.playSound(e.player.location, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
        }

        if (e.offHandItem!!.itemMeta.displayName == "§a금 생성기") {
            e.player.location.block.type = Material.GOLD_BLOCK

            val dropLocation = e.player.location

            Bukkit.getScheduler().runTaskTimer(pluginInstance!!, Runnable {
                e.player.world.dropItemNaturally(dropLocation, ItemStack(Material.GOLD_ORE))
                e.player.playSound(dropLocation, Sound.BLOCK_NOTE_BLOCK_SNARE, 100F, 1F)
            }, 0, 120)

            if (e.player.gameMode != GameMode.CREATIVE) e.player.inventory.itemInMainHand.amount--;
        }
    }

    @EventHandler
    fun onJoin(e : PlayerJoinEvent) {
        e.player.teleport(Location(e.player.world, 0.0, 103.0, 0.0))
    }
}