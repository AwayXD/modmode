package org.awayxd.modmode.commands;

import org.awayxd.modmode.ModModePlugin;
import org.awayxd.modmode.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FreezeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("modmode.freeze")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /freeze <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        PlayerInteractListener interactListener = ModModePlugin.getInstance().getInteractListener();
        UUID targetId = target.getUniqueId();
        interactListener.getFrozenPlayers().put(targetId, player.getUniqueId());
        interactListener.getFreezeEndTimes().put(targetId, Long.MAX_VALUE); // Freeze the player indefinitely

        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 1000)); // Apply slowness effect
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1000)); // Apply blindness effect
        player.sendMessage(ChatColor.AQUA + "You have frozen " + target.getName());
        target.sendMessage(ChatColor.RED + "You have been frozen by a moderator!");

        return true;
    }
}