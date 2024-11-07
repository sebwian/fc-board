package pe.swkim.fcboard.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pe.swkim.fcboard.controller.dto.CommentCreateRequest
import pe.swkim.fcboard.controller.dto.CommentUpdateRequest

private val logger = KotlinLogging.logger {}

@RestController
class CommentController {
    @PostMapping("posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long {
        logger.trace { "${commentCreateRequest.content}" }
        logger.trace { "${commentCreateRequest.createdBy}" }
        return 1L
    }

    @PutMapping("comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long {
        logger.trace { "${commentUpdateRequest.content}" }
        logger.trace { "${commentUpdateRequest.updatedBy}" }
        return commentId
    }

    @DeleteMapping("comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long {
        logger.trace { "$commentId" }
        logger.trace { "$deletedBy" }
        return commentId
    }
}
