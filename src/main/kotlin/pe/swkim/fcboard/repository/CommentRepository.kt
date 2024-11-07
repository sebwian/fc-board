package pe.swkim.fcboard.repository

import org.springframework.data.jpa.repository.JpaRepository
import pe.swkim.fcboard.domain.Comment

interface CommentRepository : JpaRepository<Comment, Long>
