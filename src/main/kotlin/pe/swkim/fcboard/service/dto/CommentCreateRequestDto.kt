package pe.swkim.fcboard.service.dto

import pe.swkim.fcboard.domain.Comment
import pe.swkim.fcboard.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) =
    Comment(
        content = content,
        createdBy = createdBy,
        post = post,
    )
