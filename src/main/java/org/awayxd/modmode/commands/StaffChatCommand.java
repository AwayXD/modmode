package org.awayxd.modmode.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.awayxd.modmode.ModModePlugin;

public class StaffChatCommand implements CommandExecutor {

    private final String STAFF_CHAT_PERMISSION = "modmode.staffchat";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(STAFF_CHAT_PERMISSION)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please provide a message.");
            return true;
        }

        String message = String.join(" ", args);
        sendStaffChatMessage(player, message);

        return true;
    }

    private void sendStaffChatMessage(Player sender, String message) {
        String formattedMessage = ChatColor.GRAY + "[STAFF] " + ChatColor.YELLOW + sender.getName() + ": " + ChatColor.WHITE + message;
        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(STAFF_CHAT_PERMISSION)) {
                onlinePlayer.sendMessage(formattedMessage);
            }
        }
    }
}
