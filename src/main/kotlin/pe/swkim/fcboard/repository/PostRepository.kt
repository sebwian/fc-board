package pe.swkim.fcboard.repository

import org.springframework.data.jpa.repository.JpaRepository
import pe.swkim.fcboard.domain.Post

interface PostRepository : JpaRepository<Post, Long>
