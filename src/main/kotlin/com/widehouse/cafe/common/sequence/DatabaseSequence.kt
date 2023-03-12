package com.widehouse.cafe.common.sequence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class DatabaseSequence(
    @Id
    val id: String,
    val seq: Long
)
