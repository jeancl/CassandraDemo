package com.example.cassandra.CassandraDemo

import com.datastax.driver.core.PagingState
import org.springframework.data.cassandra.core.query.CassandraPageRequest
import org.springframework.data.domain.Slice
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
class NotificationController(private val repositoryUnreadNotifications: ReactiveUnreadNotificationRepository) {

    @PostMapping("/api/notification")
    fun save(@RequestBody notification: UnreadNotification) = repositoryUnreadNotifications.save(notification)

    @GetMapping("/api/notification/{userId}")
    fun getNotifications(@PathVariable userId: String, @RequestParam(value = "pageSize") pageSize: Int, @RequestParam(value = "nextPage", required = false) nextPage: String?): Mono<NotificationResponseDTO> {
        val pageable: CassandraPageRequest = if(StringUtils.isEmpty(nextPage)) {
            CassandraPageRequest.first(pageSize)
        } else {
            CassandraPageRequest.of(CassandraPageRequest.first(pageSize), PagingState.fromString(nextPage))
        }
        val result: Mono<Slice<UnreadNotification>> = repositoryUnreadNotifications.findByUserId(userId, pageable)
        return result.flatMap {notification -> notificationConvert(notification)}
    }

    @DeleteMapping("/api/notification/{userId}/{id}")
    fun deleteNotification(@PathVariable userId: String, @PathVariable id: String) {
        repositoryUnreadNotifications.deleteByUserIdAndId(userId, UUID.fromString(id))
    }

    private fun notificationConvert(notification: Slice<UnreadNotification>): Mono<NotificationResponseDTO> {
        val pageRequest: CassandraPageRequest = notification.pageable as CassandraPageRequest
        return Mono.just(NotificationResponseDTO(notification.content, pageRequest.pagingState.toString()))
    }
}