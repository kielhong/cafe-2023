package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import com.widehouse.cafe.cafe.dto.CafeResponse
import com.widehouse.cafe.cafe.service.CafeService
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cafes")
class CafeController(
    private val cafeService: CafeService
) {
    @GetMapping("{url}")
    suspend fun getCafe(@PathVariable url: String): CafeResponse {
        return cafeService.getCafe(url)
    }

    @GetMapping(params = ["categoryId"])
    fun getCafesByCategoryId(@RequestParam categoryId: Long): Flow<CafeResponse> {
        return cafeService.getCafesByCategoryId(categoryId)
    }

    @PostMapping
    suspend fun create(@RequestBody cafeRequest: CafeRequest): CafeResponse {
        return cafeService.create(cafeRequest)
    }

    @PutMapping
    suspend fun update(@RequestBody request: CafeRequest): CafeResponse {
        return cafeService.update(request)
    }

    @DeleteMapping("{url}")
    suspend fun delete(@PathVariable url: String) {
        cafeService.delete(url)
    }
}
