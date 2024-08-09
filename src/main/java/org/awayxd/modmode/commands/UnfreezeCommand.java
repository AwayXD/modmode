package org.awayxd.modmode.commands;

import org.awayxd.modmode.ModModePlugin;
import org.awayxd.modmode.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class UnfreezeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("modmode.unfreeze")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /unfreeze <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        PlayerInteractListener interactListener = ModModePlugin.getInstance().getInteractListener();
        UUID targetId = target.getUniqueId();
        if (interactListener.getFrozenPlayers().remove(targetId) != null) {
            interactListener.getFreezeEndTimes().remove(targetId);
            target.removePotionEffect(PotionEffectType.SLOWNESS);
            target.removePotionEffect(PotionEffectType.BLINDNESS);
            player.sendMessage(ChatColor.AQUA + "You have unfrozen " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + "You have been unfrozen by a moderator.");
        } else {
            player.sendMessage(ChatColor.RED + "This player is not frozen.");
        }

        return true;
    }
}
