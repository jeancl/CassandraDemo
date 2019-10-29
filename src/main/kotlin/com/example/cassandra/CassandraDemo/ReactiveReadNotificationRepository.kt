package com.example.cassandra.CassandraDemo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface ReactiveReadNotificationRepository: ReactiveCrudRepository<ReadNotification, UUID> {

    fun deleteByUserIdAndId(userId: String, id: UUID): Mono<Void>

}