package pe.swkim.fcboard.exception

open class CommentException(
    message: String,
) : RuntimeException(message)

class CommentNotFoundException : PostException("댓글을 찾을 수 없습니다.")

class CommentNotUpdatableException : CommentException("댓글을 수정 할 수 없습니다")

class CommentNotDeletableException : PostException("삭제할 수 없는 댓글 입니다.")
