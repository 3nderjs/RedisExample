package ru.ender.redisexample.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ender.redisexample.Main;
import ru.ender.redisexample.utils.RedisUtil;

public class Listeners extends AstralixListener {


    public Listeners(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.startsWith("!admin")) {
            event.setCancelled(true);
            String adminMessage = ChatColor.RED + "[Admin] " + player.getName() + ": " + message.substring(6);
            broadcastMessage(adminMessage, "admin");
        } else {

            String regularMessage = ChatColor.WHITE + player.getName() + ": " + message;
            broadcastMessage(regularMessage, "regular");
        }
    }

    private void broadcastMessage(String message, String type) {
        RedisUtil redisUtil = Main.getInstance().getRedisUtil();
        String fullMessage = String.format(Main.getInstance().getServerName(), type, message);
        redisUtil.publishMessage("global_chat", fullMessage);
    }
}
