package com.example.cassandra.CassandraDemo

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ReactiveNotificationAdvancedRepository {

    fun findAllUnreadNotificationByUserId(userId: String, page: Pageable): Mono<Slice<UnreadNotification>>

    fun findAllReadNotificationByUserId(userId: String, page: Pageable): Mono<Slice<ReadNotification>>

    fun countUnreadNotificationByUserId(userId: String): Mono<NotificationCount>
}
