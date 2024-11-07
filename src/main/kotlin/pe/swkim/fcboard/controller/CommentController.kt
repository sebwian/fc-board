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
import pe.swkim.fcboard.controller.dto.toDto
import pe.swkim.fcboard.service.CommentService

private val logger = KotlinLogging.logger {}

@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long = commentService.createComment(postId, commentCreateRequest.toDto())

    @PutMapping("comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long = commentService.updateComment(commentId, commentUpdateRequest.toDto())

    @DeleteMapping("comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long = commentService.deleteComment(commentId, deletedBy)
}
