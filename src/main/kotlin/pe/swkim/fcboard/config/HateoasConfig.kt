package pe.swkim.fcboard.config

import org.springframework.context.annotation.Bean
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer

// @Configuration
// @EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class HateoasConfig {
    @Bean
    fun pagedResouceAssembler(): PagedResourcesAssembler<*> = PagedResourcesAssembler<Any>(null, null)

    @Bean
    fun pageableCustomizer(): PageableHandlerMethodArgumentResolverCustomizer =
        PageableHandlerMethodArgumentResolverCustomizer { resolver ->
            resolver.setOneIndexedParameters(true)
        }
}
