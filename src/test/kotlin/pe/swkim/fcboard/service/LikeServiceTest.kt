package pe.swkim.fcboard.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer
import pe.swkim.fcboard.domain.Post
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.LikeRepository
import pe.swkim.fcboard.repository.PostRepository

@SpringBootTest
class LikeServiceTest(
    private val likeService: LikeService,
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
        val redisContainer = GenericContainer<Nothing>("redis:latest")
        beforeSpec {
            redisContainer.portBindings.add("16379:36379")
            redisContainer.start()
            listener(redisContainer.perSpec())
        }
        afterSpec {
            redisContainer.stop()
        }
        given("좋아요 생성 시") {
            val post = postRepository.save(Post("thewall.ksw", "title", "content"))
            val likeId = likeService.createLike(post.id, "thewall.ksw")
            When("좋아요 입력 값이 정상적으로 들어오면") {
                val like = likeRepository.findByIdOrNull(likeId)
                then("좋아요가 정상적으로 생성됨을 확인한다") {
                    shouldNotBeNull { like }
                    like?.createdBy shouldBe "thewall.ksw"
                }
            }
            When("게시글이 존재하지 않으면") {
                then("존재하지 않는 게시글 예외가 발생한다") {
                    shouldThrow<PostNotFoundException> { likeService.createLike(9999L, "thewall.ksw") }
                }
            }
        }
    })
