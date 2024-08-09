package org.awayxd.modmode.listeners;

import org.awayxd.modmode.ModModePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.UUID;

public class PlayerDropPickupListener implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (ModModePlugin.getInstance().getModModePlayers().contains(playerId)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot drop items while in mod mode.");
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (ModModePlugin.getInstance().getModModePlayers().contains(playerId)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot pick up items while in mod mode.");
        }
    }
}
