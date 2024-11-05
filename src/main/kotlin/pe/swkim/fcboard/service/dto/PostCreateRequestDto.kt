package pe.swkim.fcboard.service.dto

import pe.swkim.fcboard.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequestDto.toEntity() =
    Post(
        title = title,
        content = content,
        createdBy = createdBy,
    )
