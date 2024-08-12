package org.awayxd.modmode.listeners;

import org.awayxd.modmode.ModModePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private final Map<UUID, UUID> frozenPlayers = new HashMap<>();
    private final Map<UUID, Long> freezeEndTimes = new HashMap<>();
    private final Map<UUID, Float> playerFlySpeeds = new HashMap<>(); // Store player fly speeds
    private final Map<UUID, Long> lastInventoryOpenTimes = new HashMap<>(); // Track last inventory open time

    private static final long INVENTORY_OPEN_COOLDOWN_MS = 1000; // 1 second cooldown

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the player is in mod mode
        if (!isInModMode(player)) {
            return;
        }

        // Check if the item is not null and handle it
        if (item != null) {
            Material itemType = item.getType();

            if (itemType == Material.GHAST_TEAR) {
                handleGhastTearInteraction(player, event);
            } else if (itemType == Material.ICE) {
                handleIceInteraction(player, event);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the player is in mod mode
        if (!isInModMode(player)) {
            return;
        }

        // Check if the item is not null and handle it
        if (item != null) {
            Material itemType = item.getType();

            if (itemType == Material.SUGAR) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    increaseFlySpeed(player);
                } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    decreaseFlySpeed(player);
                }
            } else if (itemType == Material.POTION) {
                toggleNightVision(player);
            }
        }
    }

    private void handleGhastTearInteraction(Player player, PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            UUID playerId = player.getUniqueId();
            UUID targetId = target.getUniqueId();

            long currentTime = System.currentTimeMillis();
            long lastOpenTime = lastInventoryOpenTimes.getOrDefault(playerId, 0L);

            if (currentTime - lastOpenTime >= INVENTORY_OPEN_COOLDOWN_MS) {
                if (!target.equals(player)) { // Ensure the player isn't trying to open their own inventory
                    player.openInventory(target.getInventory()); // Open the target player's inventory
                    player.sendMessage(ChatColor.GREEN + "Opened " + target.getName() + "'s inventory.");
                    lastInventoryOpenTimes.put(playerId, currentTime); // Update the last open time
                }
            }
        }
    }

    private void handleIceInteraction(Player player, PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            UUID targetId = target.getUniqueId();
            if (!frozenPlayers.containsKey(targetId)) { // Prevent re-freezing the same player
                frozenPlayers.put(targetId, player.getUniqueId());
                freezeEndTimes.put(targetId, Long.MAX_VALUE); // Freeze the player indefinitely
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 1000)); // Apply slowness effect
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1000)); // Apply Blindness effect
                player.sendMessage(ChatColor.AQUA + "You have frozen " + target.getName());
                target.sendMessage(ChatColor.RED + "You have been frozen by a moderator!");
            }
        }
    }

    private void increaseFlySpeed(Player player) {
        float currentSpeed = playerFlySpeeds.getOrDefault(player.getUniqueId(), 0.2f);
        if (currentSpeed < 1.0f) { // Max fly speed limit
            float newSpeed = Math.min(1.0f, currentSpeed + 0.1f);
            playerFlySpeeds.put(player.getUniqueId(), newSpeed);
            player.setFlySpeed(newSpeed);
            player.sendMessage(ChatColor.GREEN + "Fly speed increased to " + (newSpeed * 10));
        } else {
            player.sendMessage(ChatColor.RED + "Fly speed is already at maximum.");
        }
    }

    private void decreaseFlySpeed(Player player) {
        float currentSpeed = playerFlySpeeds.getOrDefault(player.getUniqueId(), 0.2f);
        if (currentSpeed > 0.1f) { // Min fly speed limit
            float newSpeed = Math.max(0.1f, currentSpeed - 0.1f);
            playerFlySpeeds.put(player.getUniqueId(), newSpeed);
            player.setFlySpeed(newSpeed);
            player.sendMessage(ChatColor.RED + "Fly speed decreased to " + (newSpeed * 10));
        } else {
            player.sendMessage(ChatColor.RED + "Fly speed is already at minimum.");
        }
    }


    private void toggleNightVision(Player player) {
        boolean hasNightVision = player.hasPotionEffect(PotionEffectType.NIGHT_VISION);
        if (hasNightVision) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(ChatColor.RED + "Night vision disabled.");
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(ChatColor.GREEN + "Night vision enabled.");
        }
    }

    private boolean isInModMode(Player player) {
        return ModModePlugin.getInstance().getModModePlayers().contains(player.getUniqueId());
    }

    public Map<UUID, UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    public Map<UUID, Long> getFreezeEndTimes() {
        return freezeEndTimes;
    }
}
