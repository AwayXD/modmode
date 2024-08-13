package org.awayxd.modmode.listeners;

import net.luckperms.api.model.user.User;
import org.awayxd.modmode.ModModePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
    private final ModModePlugin plugin;
    public JoinListener(ModModePlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage("");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("daxel.staff")) {
                User user = plugin.getLuckPerms().getUserManager().getUser(p.getUniqueId());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&5&l[ѕᴛᴀꜰꜰ] &r" + user.getCachedData().getMetaData().getSuffix() + p.getName() + " &7ᴄᴏɴɴᴇᴄᴛᴇᴅ ᴛᴏ &5&lѕᴍᴘ"));
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage("");
        if (p.hasPermission("daxel.staff")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("daxel.staff")) {
                    User user = plugin.getLuckPerms().getUserManager().getUser(p.getUniqueId());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&5&l[ѕᴛᴀꜰꜰ] &r" + user.getCachedData().getMetaData().getSuffix() + p.getName() + " &7ᴅɪѕᴄᴏɴɴᴇᴄᴛᴇᴅ ꜰʀᴏᴍ &5&lѕᴍᴘ"));
                }
            }
        }
    }
}
