package org.awayxd.modmode.commands;

import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.awayxd.modmode.ModModePlugin;

public class StaffChatCommand implements CommandExecutor {
    private final ModModePlugin plugin;

    private final String STAFF_CHAT_PERMISSION = "modmode.staffchat";

    public StaffChatCommand(ModModePlugin plugin) {
        this.plugin = plugin;
    }

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
        User user = plugin.getLuckPerms().getUserManager().getUser(sender.getUniqueId());
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', "&5&l[ѕᴛᴀꜰꜰ] &r" + user.getCachedData().getMetaData().getSuffix() + sender.getName() + "&d: " + message);
        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(STAFF_CHAT_PERMISSION)) {
                onlinePlayer.sendMessage(formattedMessage);
            }
        }
    }
}
