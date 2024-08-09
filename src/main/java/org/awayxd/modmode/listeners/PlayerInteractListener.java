package org.awayxd.modmode.listeners;

import org.awayxd.modmode.ModModePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private final Map<UUID, UUID> frozenPlayers = new HashMap<>();
    private final Map<UUID, Long> freezeEndTimes = new HashMap<>();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.ICE) {
            if (event.getRightClicked() instanceof Player) {
                Player target = (Player) event.getRightClicked();
                UUID targetId = target.getUniqueId();
                frozenPlayers.put(targetId, player.getUniqueId());
                freezeEndTimes.put(targetId, Long.MAX_VALUE); // Freeze the player indefinitely
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 1000)); // Apply slowness effect
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1000)); // Apply Blindness effect
                player.sendMessage(ChatColor.AQUA + "You have frozen " + target.getName());
                target.sendMessage(ChatColor.RED + "You have been frozen by a moderator!");
            }
        }
    }

    public Map<UUID, UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    public Map<UUID, Long> getFreezeEndTimes() {
        return freezeEndTimes;
    }
}
