package com.widehouse.cafe.common.sequence

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class SequenceService(
    private val mongoOperations: ReactiveMongoOperations
) {
    suspend fun generateSequence(seqName: String): Long {
        return mongoOperations
            .findAndModify(
                Query.query(where("_id").`is`(seqName)),
                Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence::class.java
            )
            .map { it.seq }
            .awaitSingle()
    }
}
