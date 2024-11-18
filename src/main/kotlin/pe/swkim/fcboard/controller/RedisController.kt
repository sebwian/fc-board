package pe.swkim.fcboard.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pe.swkim.fcboard.util.RedisUtil

@RestController
class RedisController(
    private val redisUtil: RedisUtil,
) {
    @GetMapping("/redis")
    fun getRedisCount(): Long {
        redisUtil.increment("count")
        return redisUtil.getCount("count") ?: 0L
    }
}
