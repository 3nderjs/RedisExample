package ru.ender.redisexample.utils;

import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import ru.ender.redisexample.Main;

public class RedisUtil {

    private final JedisPool jedisPool;
    private final Main plugin;
    private JedisPubSub jedisPubSub;
    private final String serverId;

    public RedisUtil(Main plugin, String host, int port, String serverId) {
        this.plugin = plugin;
        this.serverId = serverId;
        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    public void initialize() {
        startSubscriber();
    }

    public void shutdown() {
        if (jedisPool != null) {
            jedisPool.close();
        }
        if (jedisPubSub != null) {
            jedisPubSub.unsubscribe();
        }
    }

    public void publishMessage(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            String fullMessage = serverId + ":" + message;
            jedis.publish(channel, fullMessage);
            plugin.getLogger().info("Published message: " + fullMessage);
        }
    }

    private void startSubscriber() {
        jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals("global_chat")) {
                    String[] parts = message.split(":", 2);
                    if (parts.length == 2 && !parts[0].equals(serverId)) {
                        String actualMessage = parts[1];
                        plugin.getLogger().info("Received message: " + actualMessage);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.broadcastMessage(actualMessage);
                        });
                    }
                }
            }
        };

        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(jedisPubSub, "global_chat");
            }
        }).start();
    }
}
