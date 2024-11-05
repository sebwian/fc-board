package pe.swkim.fcboard.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pe.swkim.fcboard.domain.Post
import pe.swkim.fcboard.exception.PostNotDeletableException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.exception.PostNotUpdatableException
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.service.dto.PostCreateRequestDto
import pe.swkim.fcboard.service.dto.PostUpdateRequestDto

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
        given("게시글 생성 시") {
            When("게시글 생성") {
                val postId =
                    postService.createPost(
                        PostCreateRequestDto(
                            title = "제목",
                            content = "내용",
                            createdBy = "thewall.ksw",
                        ),
                    )
                then("게시글이 정상적으로 생성됨을 확인한다") {
                    postId shouldBeGreaterThan 0L
                    val post = postRepository.findByIdOrNull(postId)
                    post shouldNotBe null
                    post?.title shouldBe "제목"
                    post?.content shouldBe "내용"
                    post?.createdBy shouldBe "thewall.ksw"
                }
            }
        }
        given("게시글 수정 시") {
            val saved =
                postRepository.save(
                    Post(
                        title = "제목",
                        content = "내용",
                        createdBy = "thewall.ksw",
                    ),
                )
            When("정상 수정 시") {
                val updatedId =
                    postService.updatePost(
                        saved.id,
                        PostUpdateRequestDto(
                            title = "update 제목",
                            content = "update 내용",
                            updatedBy = "thewall.ksw",
                        ),
                    )
                then("게시글이 정상적으로 수정됨을 확인한다") {
                    saved.id shouldBe updatedId
                    val updated = postRepository.findByIdOrNull(updatedId)
                    updated shouldNotBe null
                    updated?.title shouldBe "update 제목"
                    updated?.content shouldBe "update 내용"
                    updated?.updatedBy shouldBe "thewall.ksw"
                }
            }
            When("게시글을 찾을 수 없을 때") {
                then("게시글을 찾을 수 없다는 예외가 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        postService.updatePost(
                            999L,
                            PostUpdateRequestDto(
                                title = "update 제목",
                                content = "update 내용",
                                updatedBy = "thewall.ksw",
                            ),
                        )
                    }
                }
            }
            When("작성자가 동일하지 않으면") {
                then("수정할 수 없는 게시글 입니다 예외가 발생한다") {
                    shouldThrow<PostNotUpdatableException> {
                        postService.updatePost(
                            saved.id,
                            PostUpdateRequestDto(
                                title = "update 제목",
                                content = "update 내용",
                                updatedBy = "chocopooding",
                            ),
                        )
                    }
                }
            }
        }
        given("게시글 삭제 시") {
            When("정상 삭제 시") {
                val saved =
                    postRepository.save(
                        Post(
                            title = "제목",
                            content = "내용",
                            createdBy = "thewall.ksw",
                        ),
                    )
                val postId = postService.deletePost(saved.id, "thewall.ksw")
                then("게시글이 정상적으로 삭제됨을 확인한다") {
                    postId shouldBe saved.id
                    postRepository.findByIdOrNull(postId) shouldBe null
                }
            }
            When("작성자가 동일하지 않으면") {
                val saved =
                    postRepository.save(
                        Post(
                            title = "제목",
                            content = "내용",
                            createdBy = "thewall.ksw",
                        ),
                    )
                then("삭제할 수 없는 게시글 입니다 예외가 발생한다") {
                    shouldThrow<PostNotDeletableException> {
                        postService.deletePost(saved.id, "chocopooding")
                    }
                }
            }
        }
    })
