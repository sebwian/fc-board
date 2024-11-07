package pe.swkim.fcboard.controller.dto

import pe.swkim.fcboard.service.dto.CommentUpdateRequestDto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDto() = CommentUpdateRequestDto(content = content, updatedBy = updatedBy)
