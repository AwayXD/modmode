package org.awayxd.modmode.commands;

import org.awayxd.modmode.ModModePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ModModeCommand implements CommandExecutor {

    private final Set<UUID> modModePlayers = new HashSet<>();
    private final Map<UUID, ItemStack[]> storedInventories = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("modmode.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        UUID playerId = player.getUniqueId();
        if (modModePlayers.contains(playerId)) {
            disableModMode(player);
        } else {
            enableModMode(player);
        }

        return true;
    }

    private void enableModMode(Player player) {
        UUID playerId = player.getUniqueId();
        modModePlayers.add(playerId);

        // Store the player's inventory
        storedInventories.put(playerId, player.getInventory().getContents());
        player.getInventory().clear();

        // Make the player invisible
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(ModModePlugin.getInstance(), player);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        player.setPlayerListName(null); // Remove player from the tab list

        // Give the player an ice block
        player.setGameMode(GameMode.CREATIVE);
        ItemStack iceBlock = new ItemStack(Material.ICE);
        ItemMeta meta = iceBlock.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Freeze Player");
        iceBlock.setItemMeta(meta);
        player.getInventory().addItem(iceBlock);

        player.sendMessage(ChatColor.GREEN + "Mod mode enabled.");
    }

    private void disableModMode(Player player) {
        UUID playerId = player.getUniqueId();
        modModePlayers.remove(playerId);

        // Restore the player's inventory
        ItemStack[] storedInventory = storedInventories.remove(playerId);
        if (storedInventory != null) {
            player.getInventory().setContents(storedInventory);
        }

        // Make the player visible again
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(ModModePlugin.getInstance(), player);
        }
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.setPlayerListName(player.getName()); // Add player back to the tab list

        // Remove the ice block
        player.getInventory().remove(Material.ICE);

        player.sendMessage(ChatColor.RED + "Mod mode disabled.");
        player.setGameMode(GameMode.SURVIVAL);
    }
}
