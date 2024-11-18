package pe.swkim.fcboard.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pe.swkim.fcboard.service.LikeService

private val logger = KotlinLogging.logger {}

@RestController
class LikeController(
    private val likeService: LikeService,
) {
    @PostMapping("posts/{postId}/likes")
    fun createLikes(
        @PathVariable postId: Long,
        @RequestParam createdBy: String,
    ) {
        logger.trace { "$createdBy $postId" }
        likeService.createLike(postId, createdBy)
    }
}
