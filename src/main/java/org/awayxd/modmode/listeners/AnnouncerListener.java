package org.awayxd.modmode.listeners;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.awayxd.modmode.ModModePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AnnouncerListener implements Listener {

    private final ModModePlugin plugin;

    public AnnouncerListener(ModModePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInModMode(player)) {
            disableModModeAnnounce(player);
        }
    }

    private boolean isInModMode(Player player) {
        return ModModePlugin.getInstance().getModModePlayers().contains(player.getUniqueId());
    }

    public void enableModModeAnnounce(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.hasPermission("modmode.use")) {
                String playerName = getColoredName(player);
                p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[ѕᴛᴀꜰꜰ] " + ChatColor.RESET + playerName + ChatColor.GREEN + " has entered mod mode.");
            }
        });
    }

    public void disableModModeAnnounce(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.hasPermission("modmode.use")) {
                String playerName = getColoredName(player);
                p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[ѕᴛᴀꜰꜰ] " + ChatColor.RESET + playerName + ChatColor.RED + " has exited mod mode.");
            }
        });
    }

    private String getColoredName(Player player) {
        UserManager userManager = plugin.getLuckPerms().getUserManager();
        User user = userManager.getUser(player.getUniqueId());

        if (user != null) {
            String prefix = user.getCachedData().getMetaData().getPrefix();
            String suffix = user.getCachedData().getMetaData().getSuffix();
            return ChatColor.translateAlternateColorCodes('&', (prefix != null ? prefix : "") + player.getName() + (suffix != null ? suffix : ""));
        }

        // Fallback to default if user data is not found
        return player.getName();
    }
}
