package pe.swkim.fcboard.controller.dto

import pe.swkim.fcboard.service.dto.PostCreateRequestDto

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequest.toDto(): PostCreateRequestDto =
    PostCreateRequestDto(
        title = this.title,
        content = this.content,
        createdBy = this.createdBy,
    )
