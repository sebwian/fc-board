package pe.swkim.fcboard.domain

import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import pe.swkim.fcboard.exception.CommentNotUpdatableException
import pe.swkim.fcboard.service.dto.CommentUpdateRequestDto

@Entity
@Table(
    name = "comment",
    indexes = [
        Index(name = "idx_post_id", columnList = "post_id"),
    ],
)
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
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var post: Post = post
        protected set

    fun update(commentUpdateRequestDto: CommentUpdateRequestDto) {
        if (commentUpdateRequestDto.updatedBy != this.createdBy) throw CommentNotUpdatableException()
        content = commentUpdateRequestDto.content
        updatedBy = commentUpdateRequestDto.updatedBy
    }
}
