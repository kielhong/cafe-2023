package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("cafes")
class CafeController(
    private val cafeService: CafeService
) {
    @GetMapping("{url}")
    fun getCafe(@PathVariable url: String): Mono<CafeResponse> {
        return cafeService.getCafe(url)
    }

    @PostMapping
    fun create(@RequestBody cafeRequest: CafeRequest): Mono<CafeResponse> {
        return cafeService.create(cafeRequest)
    }

    @PutMapping
    fun update(@RequestBody request: CafeRequest): Mono<CafeResponse> {
        return cafeService.update(request)
    }

    @DeleteMapping("{url}")
    fun delete(@PathVariable url: String): Mono<Void> {
        return cafeService.delete(url)
    }
}
