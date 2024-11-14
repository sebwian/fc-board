package pe.swkim.fcboard.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pe.swkim.fcboard.exception.PostNotDeletableException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.repository.TagRepository
import pe.swkim.fcboard.service.dto.PostCreateRequestDto
import pe.swkim.fcboard.service.dto.PostDetailResponseDto
import pe.swkim.fcboard.service.dto.PostSearchRequestDto
import pe.swkim.fcboard.service.dto.PostSummaryResponseDto
import pe.swkim.fcboard.service.dto.PostUpdateRequestDto
import pe.swkim.fcboard.service.dto.toDetailResponseDto
import pe.swkim.fcboard.service.dto.toEntity
import pe.swkim.fcboard.service.dto.toSummaryResponseDto

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val likeService: LikeService,
    private val tagRespository: TagRepository,
) {
    @Transactional
    fun createPost(postCreateRequestDto: PostCreateRequestDto): Long =
        postRepository.save(postCreateRequestDto.toEntity()).id

    @Transactional
    fun updatePost(
        updateId: Long,
        postUpdateRequestDto: PostUpdateRequestDto,
    ): Long {
        val post = postRepository.findByIdOrNull(updateId) ?: throw PostNotFoundException()
        post.update(postUpdateRequestDto)
        return post.id
    }

    @Transactional
    fun deletePost(
        deleteId: Long,
        deletedBy: String,
    ): Long {
        val post = postRepository.findByIdOrNull(deleteId) ?: throw PostNotFoundException()
        if (post.createdBy != deletedBy) throw PostNotDeletableException()
        postRepository.delete(post)
        return deleteId
    }

    fun getPost(id: Long): PostDetailResponseDto {
        val likeCount = likeService.countLike(id)
        return postRepository.findByIdOrNull(id)?.toDetailResponseDto(likeCount) ?: throw PostNotFoundException()
    }

    fun findPageBy(
        pageRequest: Pageable,
        postSearchRequestDto: PostSearchRequestDto,
    ): Page<PostSummaryResponseDto> {
        postSearchRequestDto.tag?.let { tag ->
            return tagRespository.findPageBy(pageRequest, tag).toSummaryResponseDto(likeService::countLike)
        }
        return postRepository.findPageBy(pageRequest, postSearchRequestDto).toSummaryResponseDto(likeService::countLike)
    }
}
