package org.awayxd.modmode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckAltsCommand implements CommandExecutor {

    private final FileConfiguration config;

    public CheckAltsCommand(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "player_ips.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /checkalts <playername>");
            return true;
        }

        String targetPlayerName = args[0];
        String targetIP = config.getString("ips." + targetPlayerName);

        if (targetIP == null) {
            sender.sendMessage(ChatColor.RED + "Player " + targetPlayerName + " has no recorded IP address.");
            return true;
        }

        List<String> altAccounts = new ArrayList<>();

        for (String playerName : config.getConfigurationSection("ips").getKeys(false)) {
            String ip = config.getString("ips." + playerName);
            if (ip != null && ip.equals(targetIP) && !playerName.equalsIgnoreCase(targetPlayerName)) {
                altAccounts.add(playerName);
            }
        }

        if (altAccounts.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No alternate accounts found for " + targetPlayerName + ".");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Alts for " + targetPlayerName + ":");
            for (String alt : altAccounts) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "- " + alt);
            }
        }

        return true;
    }
}
