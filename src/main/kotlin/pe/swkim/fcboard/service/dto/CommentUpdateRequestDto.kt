package pe.swkim.fcboard.service.dto

data class CommentUpdateRequestDto(
    val content: String,
    val updatedBy: String,
)

// fun CommentUpdateRequestDto.toEntity(post: Post) =
//    Comment(
//        content = content,
//        updatedBy = updatedBy,
//        post = post,
//    )
