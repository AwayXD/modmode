package org.awayxd.modmode.commands;

import org.awayxd.modmode.ModModePlugin;
import org.awayxd.modmode.listeners.AnnouncerListener;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ModModeCommand implements CommandExecutor {

    private final Set<UUID> modModePlayers = ModModePlugin.getInstance().getModModePlayers();
    private final Map<UUID, ItemStack[]> storedInventories = new HashMap<>();
    private final Map<UUID, GameMode> originalGameModes = new HashMap<>(); // Store original game modes
    private final Map<UUID, Boolean> blockBreakingEnabled = new HashMap<>(); // Track block-breaking state
    private final AnnouncerListener announcerListener; // Declare AnnouncerListener instance

    public ModModeCommand() {
        this.announcerListener = new AnnouncerListener(ModModePlugin.getInstance()); // Initialize AnnouncerListener instance with ModModePlugin
    }

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

        if (modModePlayers.contains(player.getUniqueId())) {
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

        // Store the player's original game mode and set to CREATIVE
        originalGameModes.put(playerId, player.getGameMode());
        player.setGameMode(GameMode.CREATIVE);

        // Clear the player's inventory
        player.getInventory().clear();

        // Update visibility for each player
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("modmode.use") || onlinePlayer.equals(player)) {
                // Staff and the player in mod mode can see each other
                onlinePlayer.showPlayer(ModModePlugin.getInstance(), player);
                player.showPlayer(ModModePlugin.getInstance(), onlinePlayer);
            } else {
                // Regular players cannot see mod mode players
                onlinePlayer.hidePlayer(ModModePlugin.getInstance(), player);
            }
        }

        // Make the player invisible to non-staff players
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        player.setPlayerListName(null); // Remove player from the tab list

        // Give the player items for mod mode, centered in the hotbar
        ItemStack iceBlock = new ItemStack(Material.ICE);
        ItemMeta iceMeta = iceBlock.getItemMeta();
        iceMeta.setDisplayName(ChatColor.AQUA + "Freeze Player");
        iceBlock.setItemMeta(iceMeta);
        player.getInventory().setItem(4, iceBlock); // Centered in the hotbar

        ItemStack viewInventoryItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta viewMeta = viewInventoryItem.getItemMeta();
        viewMeta.setDisplayName(ChatColor.AQUA + "View Inventory");
        viewInventoryItem.setItemMeta(viewMeta);
        player.getInventory().setItem(3, viewInventoryItem); // Adjust position as needed

        ItemStack increaseFlySpeedItem = new ItemStack(Material.SUGAR);
        ItemMeta increaseMeta = increaseFlySpeedItem.getItemMeta();
        increaseMeta.setDisplayName(ChatColor.GREEN + "Fly Speed");
        increaseFlySpeedItem.setItemMeta(increaseMeta);
        player.getInventory().setItem(5, increaseFlySpeedItem); // Adjust position as needed

        // Night vision toggle item
        ItemStack nightVisionItem = new ItemStack(Material.POTION);
        ItemMeta nightVisionMeta = nightVisionItem.getItemMeta();
        nightVisionMeta.setDisplayName(ChatColor.BLUE + "Toggle Night Vision");
        nightVisionItem.setItemMeta(nightVisionMeta);
        player.getInventory().setItem(6, nightVisionItem); // Adjust position as needed

        // Block-breaking toggle item
        ItemStack blockBreakItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta blockBreakMeta = blockBreakItem.getItemMeta();
        blockBreakMeta.setDisplayName(ChatColor.YELLOW + "Toggle Block Breaking");
        blockBreakItem.setItemMeta(blockBreakMeta);
        player.getInventory().setItem(2, blockBreakItem);

        // Set block breaking enabled by default
        blockBreakingEnabled.put(playerId, true);

        announcerListener.enableModModeAnnounce(player); // Announce when entering mod mode
    }

    private void disableModMode(Player player) {
        UUID playerId = player.getUniqueId();
        modModePlayers.remove(playerId);

        // Restore the player's inventory
        ItemStack[] storedInventory = storedInventories.remove(playerId);
        if (storedInventory != null) {
            player.getInventory().setContents(storedInventory);
        }

        // Restore the player's original game mode
        GameMode originalGameMode = originalGameModes.remove(playerId);
        if (originalGameMode != null) {
            player.setGameMode(originalGameMode);
        }

        // Make the player visible again to all
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(ModModePlugin.getInstance(), player);
        }
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.setPlayerListName(player.getName()); // Add player back to the tab list

        // Remove mod mode items
        player.getInventory().remove(Material.ICE);
        player.getInventory().remove(Material.GHAST_TEAR);
        player.getInventory().remove(Material.SUGAR);
        player.getInventory().remove(Material.POTION);
        player.getInventory().remove(Material.GRASS_BLOCK);

        // Remove block-breaking state
        blockBreakingEnabled.remove(playerId);

        announcerListener.disableModModeAnnounce(player); // Announce when exiting mod mode
    }

    public void handleBlockBreakItemClick(Player player) {
        if (modModePlayers.contains(player.getUniqueId())) {
            player.performCommand("build");
        }
    }

    public void toggleBlockBreaking(Player player) {
        UUID playerId = player.getUniqueId();
        boolean canBreakBlocks = blockBreakingEnabled.getOrDefault(playerId, true);
        blockBreakingEnabled.put(playerId, !canBreakBlocks);

        // Update item based on state
        ItemStack blockBreakItem = player.getInventory().getItem(2);
        if (blockBreakItem != null) {
            ItemMeta blockBreakMeta = blockBreakItem.getItemMeta();
            if (blockBreakMeta != null) {
                blockBreakMeta.setDisplayName((canBreakBlocks ? ChatColor.YELLOW : ChatColor.RED) + "Toggle Block Breaking");
                blockBreakItem.setItemMeta(blockBreakMeta);
            }
        }
    }

    public boolean canBreakBlocks(Player player) {
        return blockBreakingEnabled.getOrDefault(player.getUniqueId(), true);
    }
}
