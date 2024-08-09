package org.awayxd.modmode.listeners;

import org.awayxd.modmode.ModModePlugin;
import org.awayxd.modmode.commands.ModModeCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ModModeListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the item is null or if the player is not in mod mode
        if (item == null || !isInModMode(player)) {
            return;
        }

        Material itemType = item.getType();

        switch (itemType) {
            case GHAST_TEAR:
                executeInvseeCommand(player);
                break;

            case SUGAR:
                increaseFlySpeed(player);
                break;


            case ICE:
                freezePlayer(player);
                break;
        }
    }

    private boolean isInModMode(Player player) {
        return ModModePlugin.getInstance().getModModePlayers().contains(player.getUniqueId());
    }

    private void executeInvseeCommand(Player player) {
        if (!isInModMode(player)) {
            player.sendMessage(ChatColor.RED + "You need to be in mod mode to use this item.");
            return;
        }

        Player target = getTargetPlayer(player);
        if (target != null) {
            // Execute the /modmode invsee command programmatically
            String command = "invsee";
            player.performCommand(command + " " + target.getName());
            player.sendMessage(ChatColor.GREEN + "Opened " + target.getName() + "'s inventory.");
        } else {
            player.sendMessage(ChatColor.RED + "No player in sight to open inventory.");
        }
    }

    private Player getTargetPlayer(Player player) {
        return player.getNearbyEntities(10, 10, 10).stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .filter(p -> !p.equals(player)) // Exclude the mod mode player themselves
                .findFirst()
                .orElse(null);
    }

    private void increaseFlySpeed(Player player) {
        float currentSpeed = player.getFlySpeed();
        if (currentSpeed < 0.8f) { // Max fly speed limit
            player.setFlySpeed(Math.min(1.0f, currentSpeed + 0.2f)); // More noticeable increment
            player.sendMessage(ChatColor.GREEN + "Fly speed increased to " + (player.getFlySpeed() * 10));
        } else {
            player.sendMessage(ChatColor.RED + "Fly speed is already at maximum.");
        }
    }

    private void decreaseFlySpeed(Player player) {
        float currentSpeed = player.getFlySpeed();
        if (currentSpeed > 0.1f) { // Min fly speed limit
            player.setFlySpeed(Math.max(0.1f, currentSpeed - 0.2f)); // More noticeable decrement
            player.sendMessage(ChatColor.RED + "Fly speed decreased to " + (player.getFlySpeed() * 10));
        } else {
            player.sendMessage(ChatColor.RED + "Fly speed is already at minimum.");
        }
    }

    private void freezePlayer(Player modPlayer) {
        if (!isInModMode(modPlayer)) {
            modPlayer.sendMessage(ChatColor.RED + "You need to be in mod mode to use this item.");
            return;
        }

        Player target = getTargetPlayer(modPlayer);
        if (target != null) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 4, false, false));
            modPlayer.sendMessage(ChatColor.GREEN + "Frozen " + target.getName() + ".");
        } else {
            modPlayer.sendMessage(ChatColor.RED + "No player in sight to freeze.");
        }
    }
}
