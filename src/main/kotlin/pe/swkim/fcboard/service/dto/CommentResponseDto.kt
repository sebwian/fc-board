package pe.swkim.fcboard.service.dto

import pe.swkim.fcboard.domain.Comment

data class CommentResponseDto(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun Comment.toResponseDto() =
    CommentResponseDto(
        id = this.id,
        content = this.content,
        createdBy = this.createdBy,
        createdAt = this.createdAt.toString(),
    )
