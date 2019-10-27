package com.example.cassandra.CassandraDemo

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface ReactiveUnreadNotificationRepository: ReactiveCrudRepository<UnreadNotification, UUID> {

    fun deleteByUserIdAndId(userId: String, id: UUID)

    fun findByUserId(userId: String, page: Pageable):Mono<Slice<UnreadNotification>>

}