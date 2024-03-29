package com.example.cassandra.CassandraDemo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface ReactiveUnreadNotificationRepository: ReactiveCrudRepository<UnreadNotification, UUID> {

    fun findByUserIdAndId(userId: String, id: UUID):Mono<UnreadNotification>

}