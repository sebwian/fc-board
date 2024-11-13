package pe.swkim.fcboard.controller.dto

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import pe.swkim.fcboard.service.dto.PostSummaryResponseDto

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
    val tag: String? = null,
    val likeCount: Long = 0,
)

fun Page<PostSummaryResponseDto>.toResponse() =
    PageImpl(
        content.map { it.toResponse() },
        pageable,
        totalElements,
    )

fun PostSummaryResponseDto.toResponse() =
    PostSummaryResponse(
        id = this.id,
        title = this.title,
        createdBy = this.createdBy,
        createdAt = this.createdAt,
        tag = this.firstTag,
        likeCount = this.likeCount,
    )
