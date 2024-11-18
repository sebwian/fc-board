package pe.swkim.fcboard.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Value("\${spring.cache.redis.host}")
    lateinit var host: String

    @Value("\${spring.cache.redis.port}")
    lateinit var port: String

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        println("RedisConfig host: $host, port: $port}")
        return LettuceConnectionFactory(host, port.toInt())
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        return template
    }
}
