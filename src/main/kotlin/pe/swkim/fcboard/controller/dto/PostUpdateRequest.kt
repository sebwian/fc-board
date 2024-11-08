package pe.swkim.fcboard.controller.dto

import pe.swkim.fcboard.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
    val tags: List<String> = emptyList(),
)

fun PostUpdateRequest.toDto(): PostUpdateRequestDto =
    PostUpdateRequestDto(
        title = this.title,
        content = this.content,
        updatedBy = this.updatedBy,
        tags = this.tags,
    )
