package pe.swkim.fcboard.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import pe.swkim.fcboard.domain.QPost.post
import pe.swkim.fcboard.domain.QTag.tag
import pe.swkim.fcboard.domain.Tag

interface TagRepository :
    JpaRepository<Tag, Long>,
    CustomTagRepository {
    fun findByPostId(postId: Long): List<Tag>
}

interface CustomTagRepository {
    fun findPageBy(
        pageRequest: Pageable,
        tagName: String,
    ): Page<Tag>
}

class CustomTagRepositoryImpl :
    QuerydslRepositorySupport(Tag::class.java),
    CustomTagRepository {
    override fun findPageBy(
        pageRequest: Pageable,
        tagName: String,
    ): Page<Tag> =
        from(tag)
            .join(tag.post, post)
            .fetchJoin()
            .where(tag.name.eq(tagName))
            .orderBy(tag.post.createdAt.desc())
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .fetchResults()
            .let { PageImpl(it.results, pageRequest, it.total) }
}
