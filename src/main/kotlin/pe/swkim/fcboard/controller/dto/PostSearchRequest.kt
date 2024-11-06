package pe.swkim.fcboard.controller.dto

import org.springframework.web.bind.annotation.RequestParam
import pe.swkim.fcboard.service.dto.PostSearchRequestDto

data class PostSearchRequest(
    @RequestParam
    val title: String?,
    @RequestParam
    val content: String?,
    @RequestParam
    val createdBy: String?,
)

fun PostSearchRequest.toDto() =
    PostSearchRequestDto(
        title = this.title,
        createdBy = this.createdBy,
    )
