package pe.swkim.fcboard.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import pe.swkim.fcboard.domain.Post
import pe.swkim.fcboard.domain.QPost.post
import pe.swkim.fcboard.service.dto.PostSearchRequestDto

interface PostRepository :
    JpaRepository<Post, Long>,
    CustomPostRepository

interface CustomPostRepository {
    fun findPageBy(
        pageRequest: Pageable,
        postSearchRequestDto: PostSearchRequestDto,
    ): Page<Post>
}

class CustomPostRepositoryImpl :
    QuerydslRepositorySupport(Post::class.java),
    CustomPostRepository {
    override fun findPageBy(
        pageRequest: Pageable,
        postSearchRequestDto: PostSearchRequestDto,
    ): Page<Post> {
        val result =
            from(post)
                .where(
                    postSearchRequestDto.title?.let { post.title.contains(it) },
                    postSearchRequestDto.createdBy?.let { post.createdBy.contains(it) },
                ).orderBy(post.createdAt?.desc())
                .offset(pageRequest.offset)
                .limit(pageRequest.pageSize.toLong())
                .fetchResults()
        return PageImpl(result.results, pageRequest, result.total)
    }
}
