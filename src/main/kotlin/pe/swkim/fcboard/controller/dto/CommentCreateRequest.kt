package pe.swkim.fcboard.controller.dto

import pe.swkim.fcboard.service.dto.CommentCreateRequestDto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequest.toDto() = CommentCreateRequestDto(content = content, createdBy = createdBy)
