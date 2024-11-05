package pe.swkim.fcboard.service.dto

data class PostUpdateRequestDto(
    val title: String,
    val content: String,
    val updatedBy: String,
)
