package pe.swkim.fcboard.controller.dto

import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String?,
    @RequestParam
    val content: String?,
    @RequestParam
    val createdBy: String?,
)
