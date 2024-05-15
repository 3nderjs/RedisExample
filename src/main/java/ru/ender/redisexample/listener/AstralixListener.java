package ru.ender.redisexample.listener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AstralixListener
        implements Listener {
    private final JavaPlugin plugin;

    public AstralixListener(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);


    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}