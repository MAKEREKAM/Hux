package kr.vanilage.main

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class Missile : Listener {
    @EventHandler
    fun onInteract(e : PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR) return
        if (e.player.inventory.itemInMainHand.type != Material.CREEPER_HEAD && e.player.inventory.itemInMainHand.type != Material.PLAYER_HEAD) return
        val entity : Snowball = e.player.launchProjectile(Snowball::class.java)
        entity.isVisibleByDefault = false
        entity.velocity = e.player.location.direction.multiply(5)
        if (e.player.inventory.itemInMainHand.type == Material.CREEPER_HEAD) entity.addScoreboardTag("missile")
        if (e.player.inventory.itemInMainHand.type == Material.PLAYER_HEAD) entity.addScoreboardTag("super_missile")
        entity.addScoreboardTag("gameProjectile")
        val displayBlock : ItemDisplay = entity.world.spawn(entity.location, ItemDisplay::class.java)
        val head : ItemStack = ItemStack(Material.PLAYER_HEAD);
        val meta : SkullMeta = head.itemMeta as SkullMeta
        meta.owningPlayer = e.player
        head.itemMeta = meta

        displayBlock.itemStack = head

        displayBlock.billboard = Display.Billboard.VERTICAL

        Bukkit.getScheduler().runTaskTimer(pluginInstance!!, Runnable {
            if (entity.isDead) {
                displayBlock.remove()
            }

            else {
                entity.world.spawnParticle(Particle.FLAME, entity.location, 15, 0.1, 0.0, 0.1)
                displayBlock.teleport(entity.location)
            }
        }, 0, 1)
        if (e.player.gameMode != GameMode.CREATIVE) e.player.inventory.itemInMainHand.amount--
    }

    @EventHandler
    fun onProjectileHit(e : ProjectileHitEvent) {
        if (!e.entity.scoreboardTags.contains("gameProjectile")) return

        val damage = if (e.entity.scoreboardTags.contains("missile")) { 4 } else 8

        e.entity.world.createExplosion(e.entity.location, damage.toFloat())

        if (e.hitBlock!!.location == Data.RED_CORE) {
            Main.RED_CORE_HP -= damage
            if (Main.RED_CORE_HP <= 0) {
                Bukkit.broadcastMessage("RED 탈락")
            }
        }

        if (e.hitBlock!!.location == Data.BLUE_CORE) {
            Main.BLUE_CORE_HP -= damage
            if (Main.BLUE_CORE_HP <= 0) {
                Bukkit.broadcastMessage("BLUE 탈락")
            }
        }
    }
}