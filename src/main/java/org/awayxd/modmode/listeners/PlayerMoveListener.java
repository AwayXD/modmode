package org.awayxd.modmode.listeners;

import org.awayxd.modmode.ModModePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

public class PlayerMoveListener implements Listener {

    private final Map<UUID, Location> playerLocations = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        PlayerInteractListener interactListener = ModModePlugin.getInstance().getInteractListener();

        if (interactListener.getFrozenPlayers().containsKey(playerId)) {
            long freezeEndTime = interactListener.getFreezeEndTimes().get(playerId);
            if (System.currentTimeMillis() < freezeEndTime) {
                if (!playerLocations.containsKey(playerId)) {
                    playerLocations.put(playerId, player.getLocation());
                } else {
                    player.teleport(playerLocations.get(playerId));
                    player.sendMessage(ChatColor.RED + "You are frozen and cannot move!");
                }
            } else {
                interactListener.getFrozenPlayers().remove(playerId);
                interactListener.getFreezeEndTimes().remove(playerId);
                playerLocations.remove(playerId);
            }
        }
    }
}
