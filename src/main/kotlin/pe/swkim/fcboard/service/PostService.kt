package pe.swkim.fcboard.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pe.swkim.fcboard.exception.PostNotDeletableException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.service.dto.PostCreateRequestDto
import pe.swkim.fcboard.service.dto.PostUpdateRequestDto
import pe.swkim.fcboard.service.dto.toEntity

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
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
}
