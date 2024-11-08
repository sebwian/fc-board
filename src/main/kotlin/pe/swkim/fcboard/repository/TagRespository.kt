package pe.swkim.fcboard.repository

import org.springframework.data.jpa.repository.JpaRepository
import pe.swkim.fcboard.domain.Tag

interface TagRespository : JpaRepository<Tag, Long> {
    fun findByPostId(postId: Long): List<Tag>
}
