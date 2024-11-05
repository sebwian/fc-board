package pe.swkim.fcboard.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pe.swkim.fcboard.controller.dto.PostCreateRequest
import pe.swkim.fcboard.controller.dto.PostDetailResponse
import pe.swkim.fcboard.controller.dto.PostSearchRequest
import pe.swkim.fcboard.controller.dto.PostSummaryResponse
import pe.swkim.fcboard.controller.dto.PostUpdateRequest
import pe.swkim.fcboard.controller.dto.toDto
import pe.swkim.fcboard.service.PostService
import java.time.LocalDateTime

@RestController
class PostController(
    private val postService: PostService,
) {
    @PostMapping("/posts")
    fun createPost(
        @RequestBody postCreateRequest: PostCreateRequest,
    ): Long = postService.createPost(postCreateRequest.toDto())

    @PutMapping("/posts/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest,
    ): Long = postService.updatePost(id, postUpdateRequest.toDto())

    @DeleteMapping("/posts/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long = postService.deletePost(id, createdBy)

    @GetMapping("/posts/{id}")
    fun getPost(
        @PathVariable id: Long,
    ): PostDetailResponse =
        PostDetailResponse(
            id = 1L,
            title = "title",
            content = "content",
            createdBy = "thewall.ksw",
            createdAt = LocalDateTime.now().toString(),
        )

    @GetMapping("/posts")
    fun getPosts(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        println("title: ${postSearchRequest.title}")
        println("createdBy: ${postSearchRequest.createdBy}")
        return Page.empty()
    }
}
