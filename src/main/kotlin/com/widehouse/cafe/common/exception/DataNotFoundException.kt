package com.widehouse.cafe.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class DataNotFoundException : RuntimeException {
    constructor(message: String) : super(message)
}
