package pe.swkim.fcboard.event

import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import pe.swkim.fcboard.domain.Like
import pe.swkim.fcboard.event.dto.LikeEvent
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.LikeRepository
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.util.RedisUtil

@Service
class LikeEventHandler(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val redisUtil: RedisUtil,
) {
    @Async
    @TransactionalEventListener(LikeEvent::class)
    fun handle(event: LikeEvent) {
        Thread.sleep(3000)
        val post = postRepository.findByIdOrNull(event.postId) ?: throw PostNotFoundException()
        redisUtil.increment(redisUtil.getLikeCountKey(event.postId))
        likeRepository.save(Like(post, event.createdBy)).id
    }
}
