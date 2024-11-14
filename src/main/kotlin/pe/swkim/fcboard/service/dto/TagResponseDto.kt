package pe.swkim.fcboard.service.dto

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import pe.swkim.fcboard.domain.Tag

fun Page<Tag>.toSummaryResponseDto(fnCountLike: (Long) -> Long) =
    PageImpl(
        content.map { it.toSummaryResponseDto(fnCountLike) },
        pageable,
        totalElements,
    )

fun Tag.toSummaryResponseDto(fnCountLike: (Long) -> Long) =
    PostSummaryResponseDto(
        id = post.id,
        title = post.title,
        createdBy = post.createdBy,
        createdAt = post.createdAt.toString(),
        likeCount = fnCountLike(post.id),
    )
