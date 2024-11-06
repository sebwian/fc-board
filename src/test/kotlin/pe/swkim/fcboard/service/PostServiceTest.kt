package pe.swkim.fcboard.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import pe.swkim.fcboard.domain.Post
import pe.swkim.fcboard.exception.PostNotDeletableException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.exception.PostNotUpdatableException
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.service.dto.PostCreateRequestDto
import pe.swkim.fcboard.service.dto.PostSearchRequestDto
import pe.swkim.fcboard.service.dto.PostUpdateRequestDto

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
        beforeSpec {
            postRepository.saveAll(
                listOf(
                    Post(title = "title1", content = "content1", createdBy = "thewall.ksw"),
                    Post(title = "title12", content = "content2", createdBy = "thewall.ksw"),
                    Post(title = "title13", content = "content3", createdBy = "thewall.ksw"),
                    Post(title = "title14", content = "content4", createdBy = "thewall.ksw"),
                    Post(title = "title15", content = "content5", createdBy = "thewall.ksw"),
                    Post(title = "title6", content = "content6", createdBy = "chochopooding"),
                    Post(title = "title7", content = "content7", createdBy = "chochopooding"),
                    Post(title = "title8", content = "content8", createdBy = "chochopooding"),
                    Post(title = "title9", content = "content9", createdBy = "chochopooding"),
                    Post(title = "title20", content = "content10", createdBy = "chochopooding"),
                ),
            )
        }
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
        given("게시글 상세 조회 시") {
            val saved =
                postRepository.save(
                    Post(
                        title = "제목",
                        content = "내용",
                        createdBy = "thewall.ksw",
                    ),
                )
            When("정상 조회 시") {
                val post = postService.getPost(saved.id)
                then("게시글의 내용이 정상적으로 반환됨을 확인한다") {
                    post.id shouldBe saved.id
                    post.title shouldBe saved.title
                    post.content shouldBe saved.content
                    post.createdBy shouldBe saved.createdBy
                }
            }
            When("게시글이 없을 때") {
                then("게시글을 찾을 수 없습니다 예외가 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        postService.getPost(999L)
                    }
                }
            }
        }
        given("게시글 목록 조회 시") {
            When("정상 조회 시") {
                val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto())
                then("게시글 페이지가 반환된다") {
                    postPage.number shouldBe 0
                    postPage.size shouldBe 5
                    postPage.content.size shouldBe 5
                    postPage.content[0].title shouldContain "제목"
                    postPage.content[0].createdBy shouldBe "thewall.ksw"
                }
            }
            When("타이틀로 검색") {
                val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))
                then("타이틀에 해당하는 게시글이 반환된다") {
                    postPage.number shouldBe 0
                    postPage.size shouldBe 5
                    postPage.content.size shouldBe 5
                    postPage.content[0].title shouldContain "title1"
                    postPage.content[0].createdBy shouldContain "thewall.ksw"
                }
            }
            When("작성자로 검색") {
                val postPage =
                    postService.findPageBy(
                        PageRequest.of(0, 5),
                        PostSearchRequestDto(createdBy = "thewall.ksw"),
                    )
                then("작성자에 해당하는 게시글이 반환된다") {
                    postPage.number shouldBe 0
                    postPage.size shouldBe 5
                    postPage.content.size shouldBe 5
                    postPage.content[0].title shouldContain "제목"
                    postPage.content[0].createdBy shouldBe "thewall.ksw"
                }
            }
        }
    })
