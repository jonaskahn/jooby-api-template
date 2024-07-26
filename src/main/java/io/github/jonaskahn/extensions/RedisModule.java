package io.github.jonaskahn.extensions;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.jooby.Extension;
import io.jooby.Jooby;
import lombok.SneakyThrows;
import redis.clients.jedis.JedisPooled;

public class RedisModule implements Extension {

    @Override
    @SneakyThrows(Exception.class)
    public void install(@NonNull Jooby application) {
        var redisHost = application.getConfig().getString("redis.host");
        var redisPort = application.getConfig().getInt("redis.port");
        var redis = new JedisPooled(redisHost, redisPort);
        var registry = application.getServices();
        registry.put(JedisPooled.class, redis);
        application.onStop(redis::close);
    }
}
