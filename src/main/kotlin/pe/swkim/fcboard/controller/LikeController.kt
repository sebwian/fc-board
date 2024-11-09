package pe.swkim.fcboard.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
class LikeController {
    @PostMapping("posts/{postId}/likes")
    fun createLikes(
        @PathVariable postId: Long,
        @RequestParam createdBy: String,
    ): Long {
        logger.trace { "$createdBy $postId" }
        return 1L
    }
}
