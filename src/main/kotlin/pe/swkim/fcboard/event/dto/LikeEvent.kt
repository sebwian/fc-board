package pe.swkim.fcboard.event.dto

data class LikeEvent(
    val postId: Long,
    val createdBy: String,
)
