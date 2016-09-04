package hu.poketerkep.master.config;

import hu.poketerkep.shared.datasource.PokemonDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean
    JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @Bean
    PokemonDataSource pokemonDataSource() {
        return new PokemonDataSource(jedisPool());
    }
}
