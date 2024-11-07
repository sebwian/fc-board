package pe.swkim.fcboard.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pe.swkim.fcboard.exception.CommentNotDeletableException
import pe.swkim.fcboard.exception.CommentNotFoundException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.CommentRepository
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.service.dto.CommentCreateRequestDto
import pe.swkim.fcboard.service.dto.CommentUpdateRequestDto
import pe.swkim.fcboard.service.dto.toEntity

@Service
@Transactional(readOnly = true)
class CommentService(
    val commentRepository: CommentRepository,
    val postRepository: PostRepository,
) {
    @Transactional
    fun createComment(
        postId: Long,
        commentCreateRequestDto: CommentCreateRequestDto,
    ): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return commentRepository.save(commentCreateRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(
        commentId: Long,
        commentUpdateRequestDto: CommentUpdateRequestDto,
    ): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        comment.update(commentUpdateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(
        commentId: Long,
        deletedBy: String,
    ): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        if (comment.createdBy != deletedBy) throw CommentNotDeletableException()
        commentRepository.delete(comment)
        return commentId
    }
}
