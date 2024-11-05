package pe.swkim.fcboard.controller.dto

import pe.swkim.fcboard.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)

fun PostUpdateRequest.toDto(): PostUpdateRequestDto =
    PostUpdateRequestDto(
        title = this.title,
        content = this.content,
        updatedBy = this.updatedBy,
    )
