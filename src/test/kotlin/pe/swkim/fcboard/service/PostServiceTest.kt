package pe.swkim.fcboard.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer
import pe.swkim.fcboard.domain.Comment
import pe.swkim.fcboard.domain.Post
import pe.swkim.fcboard.domain.Tag
import pe.swkim.fcboard.exception.PostNotDeletableException
import pe.swkim.fcboard.exception.PostNotFoundException
import pe.swkim.fcboard.exception.PostNotUpdatableException
import pe.swkim.fcboard.repository.CommentRepository
import pe.swkim.fcboard.repository.PostRepository
import pe.swkim.fcboard.repository.TagRepository
import pe.swkim.fcboard.service.dto.PostCreateRequestDto
import pe.swkim.fcboard.service.dto.PostSearchRequestDto
import pe.swkim.fcboard.service.dto.PostUpdateRequestDto

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRespository: TagRepository,
    private val likeService: LikeService,
) : BehaviorSpec() {
    init {
        val redisContainer =
            GenericContainer<Nothing>("redis:7.4.1-alpine").apply {
                portBindings.add("16379:6379")
            }
//            GenericContainer<Nothing>("redis:7.4.1-alpine").apply {
// //                withExposedPorts(6379)
// //                waitingFor(LogMessageWaitStrategy().withRegEx(".*Ready to accept connections tcp\\s").withTimes(2))
//                portBindings.add("16379:6379")
//            }

        afterSpec {
            redisContainer.stop()
        }

        beforeSpec {
//            redisContainer.portBindings.add("16379:6379")
            redisContainer.start()

//            println("MappedPort:${redisContainer.getMappedPort(6379)}")
//            System.setProperty("spring.cache.redis.port", redisContainer.getMappedPort(6379).toString())
            listener(redisContainer.perSpec())

            postRepository.saveAll(
                listOf(
                    Post(
                        title = "title1",
                        content = "content1",
                        createdBy = "thewall.ksw",
                        tags = listOf("tag1", "tag2"),
                    ),
                    Post(
                        title = "title12",
                        content = "content2",
                        createdBy = "thewall.ksw",
                        tags = listOf("tag1", "tag2"),
                    ),
                    Post(
                        title = "title13",
                        content = "content3",
                        createdBy = "thewall.ksw",
                        tags = listOf("tag1", "tag2"),
                    ),
                    Post(
                        title = "title14",
                        content = "content4",
                        createdBy = "thewall.ksw",
                        tags = listOf("tag1", "tag2"),
                    ),
                    Post(
                        title = "title15",
                        content = "content5",
                        createdBy = "thewall.ksw",
                        tags = listOf("tag1", "tag2"),
                    ),
                    Post(
                        title = "title6",
                        content = "content6",
                        createdBy = "chochopooding",
                        tags = listOf("tag1", "tag5"),
                    ),
                    Post(
                        title = "title7",
                        content = "content7",
                        createdBy = "chochopooding",
                        tags = listOf("tag1", "tag5"),
                    ),
                    Post(
                        title = "title8",
                        content = "content8",
                        createdBy = "chochopooding",
                        tags = listOf("tag1", "tag5"),
                    ),
                    Post(
                        title = "title9",
                        content = "content9",
                        createdBy = "chochopooding",
                        tags = listOf("tag1", "tag5"),
                    ),
                    Post(
                        title = "title20",
                        content = "content20",
                        createdBy = "chochopooding",
                        tags = listOf("tag1", "tag5"),
                    ),
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
            When("태그가 추가되면") {
                val postId =
                    postService.createPost(
                        PostCreateRequestDto(
                            title = "제목",
                            content = "내용",
                            createdBy = "thewall.ksw",
                            tags = listOf("tag1", "tag2"),
                        ),
                    )
                then("'태그가 정상적으로 추가됨을 확인한다") {
                    val tags = tagRespository.findByPostId(postId)
                    tags.size shouldBe 2
                    tags[0].name shouldBe "tag1"
                    tags[1].name shouldBe "tag2"
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
                        tags = listOf("tag1", "tag2"),
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
            When("태그가 수정 되었을 때") {
                val updatedId =
                    postService.updatePost(
                        saved.id,
                        PostUpdateRequestDto(
                            title = "update 제목",
                            content = "update 내용",
                            updatedBy = "thewall.ksw",
                            tags = listOf("tag1", "tag2", "tag3"),
                        ),
                    )
                then("정상적으로 수정됨을 확인한다") {
                    val tags = tagRespository.findByPostId(updatedId)
                    tags.size shouldBe 3
                    tags[2].name shouldBe "tag3"
                }
                then("태그 순서가 변경되었을때 정상적으로 변경됨을 확인한다") {
                    postService.updatePost(
                        saved.id,
                        PostUpdateRequestDto(
                            title = "update 제목",
                            content = "update 내용",
                            updatedBy = "thewall.ksw",
                            tags = listOf("tag3", "tag2", "tag1"),
                        ),
                    )
                    val tags = tagRespository.findByPostId(updatedId)
                    tags.size shouldBe 3
                    tags[2].name shouldBe "tag1"
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
            tagRespository.saveAll(
                listOf(
                    Tag(name = "tag1", saved, saved.createdBy),
                    Tag(name = "tag2", saved, saved.createdBy),
                    Tag(name = "tag3", saved, saved.createdBy),
                ),
            )
            likeService.createLike(saved.id, "thewall.ksw")
            likeService.createLike(saved.id, "chocopooding")
            likeService.createLike(saved.id, "heesis")
            When("정상 조회 시") {
                Thread.sleep(500) // async 로 인한 차이 적용
                val post = postService.getPost(saved.id)
                then("게시글의 내용이 정상적으로 반환됨을 확인한다") {
                    post.id shouldBe saved.id
                    post.title shouldBe saved.title
                    post.content shouldBe saved.content
                    post.createdBy shouldBe saved.createdBy
                }
                then("태그가 정상적으로 조회됨을 확인한다") {
                    post.tags.size shouldBe 3
                    post.tags[0] shouldBe "tag1"
                    post.tags[1] shouldBe "tag2"
                    post.tags[2] shouldBe "tag3"
                }
                then("좋아요 개수가 정상적으로 조회됨을 확인한다") {
                    post.likeCount shouldBe 3
                }
            }
            When("게시글이 없을 때") {
                then("게시글을 찾을 수 없습니다 예외가 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        postService.getPost(999L)
                    }
                }
            }
            When("댓글 추가 시") {
                val comment1 = commentRepository.save(Comment("댓글 내용 1", saved, "thewall.ksw"))
                val comment2 = commentRepository.save(Comment("댓글 내용 2", saved, "thewall.ksw"))
                val comment3 = commentRepository.save(Comment("댓글 내용 3", saved, "thewall.ksw"))
                val post = postService.getPost(saved.id)
                then("댓글이 함께 조회됨을 확인한다") {
                    post.comments.size shouldBe 3
                    post.comments[0].content shouldBe comment1.content
                    post.comments[1].content shouldBe comment2.content
                    post.comments[2].content shouldBe comment3.content
                    post.comments[0].createdBy shouldBe comment1.createdBy
                    post.comments[1].createdBy shouldBe comment2.createdBy
                    post.comments[2].createdBy shouldBe comment3.createdBy
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
                        PostSearchRequestDto(createdBy = "thewall.ksw", title = "title1"),
                    )
                then("작성자에 해당하는 게시글이 반환된다") {
                    postPage.number shouldBe 0
                    postPage.size shouldBe 5
                    postPage.content.size shouldBe 5
                    postPage.content[0].title shouldContain "title1"
                    postPage.content[0].createdBy shouldBe "thewall.ksw"
                }
                then("첫번째 태그가 함께 조회됨을 확인한다") {
                    postPage.content.forEach {
                        it.firstTag shouldBe "tag1"
                    }
                }
            }
            When("태그로 검색") {
                val postPage =
                    postService.findPageBy(
                        PageRequest.of(0, 5),
                        PostSearchRequestDto(tag = "tag5"),
                    )
                then("태그에 해당하는 게시글이 반환된다") {
                    postPage.number shouldBe 0
                    postPage.size shouldBe 5
                    postPage.content.size shouldBe 5
                    postPage.content[0].title shouldContain "title6"
                    postPage.content[1].title shouldContain "title7"
                }
            }
            When("종아요가 2개 추가되었을 때") {
                val postPage =
                    postService.findPageBy(
                        PageRequest.of(0, 5),
                        PostSearchRequestDto(tag = "tag5"),
                    )
                postPage.forEach {
                    likeService.createLike(it.id, "thewall.ksw")
                    likeService.createLike(it.id, "chocopooding")
                }
                Thread.sleep(200)
                val likePostPage =
                    postService.findPageBy(
                        PageRequest.of(0, 5),
                        PostSearchRequestDto(tag = "tag5"),
                    )
                then("좋아요 개수가 정상적으로 조회됨을 확인한다") {
                    likePostPage.content.forEach { it.likeCount shouldBe 2 }
                }
            }
        }
    }
}
