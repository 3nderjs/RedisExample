package ru.ender.redisexample;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import ru.ender.redisexample.listener.Listeners;
import ru.ender.redisexample.utils.RedisUtil;

public final class Main extends JavaPlugin {
    private static Main instance;
    private RedisUtil redisUtil;
    private String serverName;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        serverName = getConfig().getString("server.name");
        String redisHost = getConfig().getString("redis.host");
        int redisPort = getConfig().getInt("redis.port");

        redisUtil = new RedisUtil(this, redisHost, redisPort, serverName);
        redisUtil.initialize();

        new Listeners(this);

    }

    @Override
    public void onDisable() {
        if (redisUtil != null) {
            redisUtil.shutdown();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public String getServerName() {
        return serverName;
    }
}