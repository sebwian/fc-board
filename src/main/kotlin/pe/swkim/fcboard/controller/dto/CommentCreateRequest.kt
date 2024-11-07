package pe.swkim.fcboard.controller.dto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)
