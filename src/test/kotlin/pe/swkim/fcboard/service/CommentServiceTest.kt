package pe.swkim.fcboard.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer
import pe.swkim.fcboard.domain.Comment
import pe.swkim.fcboard.domain.Post
import pe.swkim.fcboard.exception.CommentNotDeletableException
import pe.swkim.fcboard.exception.CommentNotUpdatableException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.repository.CommentRepository
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.service.dto.CommentCreateRequestDto
import pe.swkim.fcboard.service.dto.CommentUpdateRequestDto

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
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
        given("댓글 생성 시") {
            val commentCreateRequestDto =
                CommentCreateRequestDto(
                    content = "댓글 내용",
                    createdBy = "댓글 작성자",
                )
            When("댓글이 정상적으로 들어오면") {
                val post =
                    postRepository
                        .save(
                            Post(
                                title = "게시글",
                                content = "게시 내용",
                                createdBy = "thewall.ksw",
                            ),
                        )
                val commentId =
                    commentService.createComment(
                        postId = post.id,
                        commentCreateRequestDto,
                    )
                then("정상 생성됨을 확인한다") {
                    commentId shouldBeGreaterThan 0
                    val comment = commentRepository.findByIdOrNull(commentId) shouldNotBe null
                    shouldNotBeNull { comment }
                    comment?.content shouldBe commentCreateRequestDto.content
                    comment?.createdBy shouldBe commentCreateRequestDto.createdBy
                }
            }
            When("게시글이 존재하지 않으면") {
                then("게시글이 존재하지 않습니다 예외가 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        commentService.createComment(
                            postId = 9999L,
                            commentCreateRequestDto,
                        )
                    }
                }
            }
        }
        given("댓글 수정 시") {
            val post =
                postRepository
                    .save(
                        Post(
                            title = "게시글",
                            content = "게시 내용",
                            createdBy = "thewall.ksw",
                        ),
                    )
            val saved = commentRepository.save(Comment("댓글 내용", post, "thewall.ksw"))
            When("댓글이 정상적으로 들어오면") {
                val updatedId =
                    commentService.updateComment(
                        commentId = saved.id,
                        commentUpdateRequestDto =
                            CommentUpdateRequestDto(
                                content = "수정된 댓글 내용",
                                updatedBy = "thewall.ksw",
                            ),
                    )
                then("정상 수정됨을 확인한다") {
                    updatedId shouldBe saved.id
                    val updated = commentRepository.findByIdOrNull(updatedId)
                    shouldNotBeNull { updated }
                    updated?.content shouldBe "수정된 댓글 내용"
                    updated?.createdBy shouldBe saved.createdBy
                }
            }
            When("작성자와 수정자가 다른면") {
                then("수정할 수 없는 댓글입니다 예외가 발생한다") {
                    shouldThrow<CommentNotUpdatableException> {
                        commentService.updateComment(
                            commentId = saved.id,
                            commentUpdateRequestDto =
                                CommentUpdateRequestDto(
                                    content = "수정된 댓글 내용",
                                    updatedBy = "chocopooding",
                                ),
                        )
                    }
                }
            }
        }
        given("대글 삭제 시") {
            val post =
                postRepository
                    .save(
                        Post(
                            title = "게시글",
                            content = "게시 내용",
                            createdBy = "thewall.ksw",
                        ),
                    )
            val saved = commentRepository.save(Comment("댓글 내용", post, "thewall.ksw"))
            val saved2 = commentRepository.save(Comment("댓글 내용2", post, "chocopooding"))
            When("댓글이 정상적으로 들어오면") {
                then("댓글이 정상 삭제됨을 확인한다") {
                    val commentId = commentService.deleteComment(saved.id, "thewall.ksw")
                    commentId shouldBeGreaterThan 0
                    commentId shouldBe saved.id
                    commentRepository.findByIdOrNull(commentId) shouldBe null
                }
            }
            When("작성자와 삭제자가 다르면") {
                then("삭제할 수 없은 댓글 입니다 예외를 발생한다") {
                    shouldThrow<CommentNotDeletableException> {
                        commentService.deleteComment(saved2.id, "thewall.ksw")
                    }
                }
            }
        }
    })
