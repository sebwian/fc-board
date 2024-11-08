package pe.swkim.fcboard.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pe.swkim.fcboard.domain.Like
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.LikeRepository
import pe.swkim.fcboard.repository.PostRepository

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createLike(
        postId: Long,
        createdBy: String,
    ): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return likeRepository.save(Like(post, createdBy)).id
    }
}
