package com.widehouse.cafe.common

import com.widehouse.cafe.config.SecurityConfig
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.context.annotation.Import

@Import(SecurityConfig::class)
open class SecurityControllerTest() : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)
}
