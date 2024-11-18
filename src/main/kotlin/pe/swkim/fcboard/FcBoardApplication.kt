package pe.swkim.fcboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

// @EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableAsync
@SpringBootApplication
class FcBoardApplication

fun main(args: Array<String>) {
    runApplication<FcBoardApplication>(*args)
}
