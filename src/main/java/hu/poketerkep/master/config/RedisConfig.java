package hu.poketerkep.master.config;

import hu.poketerkep.shared.datasource.PokemonDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Configuration
    @Profile("default")
    public class ProductionRedisConfig {
        @Bean
        JedisPool jedisPool() {
            return new JedisPool(new JedisPoolConfig(), "172.30.1.104");
        }

        @Bean
        PokemonDataSource pokemonDataSource() {
            return new PokemonDataSource(jedisPool());
        }
    }

    @Configuration
    @Profile("development")
    public class DevelopmentRedisConfig {
        @Bean
        JedisPool jedisPool() {
            return new JedisPool(new JedisPoolConfig(), "localhost");
        }

        @Bean
        PokemonDataSource pokemonDataSource() {
            return new PokemonDataSource(jedisPool());
        }
    }
}
