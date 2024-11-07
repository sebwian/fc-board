package pe.swkim.fcboard.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import pe.swkim.fcboard.exception.CommentNotUpdatableException
import pe.swkim.fcboard.service.dto.CommentUpdateRequestDto

@Entity
class Comment(
    content: String,
    post: Post,
    createdBy: String,
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post = post
        protected set

    fun update(commentUpdateRequestDto: CommentUpdateRequestDto) {
        if (commentUpdateRequestDto.updatedBy != this.createdBy) throw CommentNotUpdatableException()
        content = commentUpdateRequestDto.content
        updatedBy = commentUpdateRequestDto.updatedBy
    }
}
