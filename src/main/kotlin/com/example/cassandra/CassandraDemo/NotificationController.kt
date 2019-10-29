package com.example.cassandra.CassandraDemo

import com.datastax.driver.core.PagingState
import org.springframework.data.cassandra.core.query.CassandraPageRequest
import org.springframework.data.domain.Slice
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
class NotificationController(private val repositoryNotificationsUnread: ReactiveUnreadNotificationRepository,
                             private val repositoryNotificationsReadNotification: ReactiveReadNotificationRepository,
                             private val advacedRepositoryNotifications: ReactiveNotificationAdvancedRepository) {

    @PostMapping("/api/notification")
    fun save(@RequestBody notification: UnreadNotification) = repositoryNotificationsUnread.save(notification)

    @GetMapping("/api/notification/unread/{userId}")
    fun getUnreadNotifications(@PathVariable userId: String, @RequestParam(value = "pageSize") pageSize: Int, @RequestParam(value = "nextPage", required = false) nextPage: String?): Mono<NotificationResponseDTO> {
        val pageable: CassandraPageRequest = if(StringUtils.isEmpty(nextPage)) {
            CassandraPageRequest.first(pageSize)
        } else {
            CassandraPageRequest.of(CassandraPageRequest.first(pageSize), PagingState.fromString(nextPage))
        }
        val result: Mono<Slice<UnreadNotification>> = advacedRepositoryNotifications.findAllUnreadNotificationByUserId(userId, pageable)
        return result.flatMap {notification -> unreadNotificationConvert(notification)}
    }

    @GetMapping("/api/notification/read/{userId}")
    fun getReadNotifications(@PathVariable userId: String, @RequestParam(value = "pageSize") pageSize: Int, @RequestParam(value = "nextPage", required = false) nextPage: String?): Mono<NotificationResponseDTO> {
        val pageable: CassandraPageRequest = if(StringUtils.isEmpty(nextPage)) {
            CassandraPageRequest.first(pageSize)
        } else {
            CassandraPageRequest.of(CassandraPageRequest.first(pageSize), PagingState.fromString(nextPage))
        }
        val result: Mono<Slice<ReadNotification>> = advacedRepositoryNotifications.findAllReadNotificationByUserId(userId, pageable)
        return result.flatMap {notification -> readNotificationConvert(notification)}
    }

    @GetMapping("/api/notification/{userId}/count")
    fun getCountUnreadNotifications(@PathVariable userId: String) = advacedRepositoryNotifications.countUnreadNotificationByUserId(userId)

    @PutMapping("/api/notification/{userId}/{id}")
    fun updateNotificationRead(@PathVariable userId: String, @PathVariable id: String): Mono<UnreadNotification> {
        return repositoryNotificationsUnread.findByUserIdAndId(userId, UUID.fromString(id)).doOnSuccess { notification ->
            val readNotification = ReadNotification(notification.id, notification.userId, notification.createDate, notification.notificationType,
                    notification.notificationFunction, notification.messageHeader, notification.updtUserId, notification.updtCompanyId, notification.recordCount,
                    notification.recordDate)
            repositoryNotificationsReadNotification.save(readNotification).subscribe()
            repositoryNotificationsUnread.delete(notification).subscribe()
        }
    }

    @DeleteMapping("/api/notification/{userId}/{id}")
    fun deleteNotification(@PathVariable userId: String, @PathVariable id: String): Mono<Void> {
        return repositoryNotificationsReadNotification.deleteByUserIdAndId(userId, UUID.fromString(id)).doOnSuccess { System.out.println("Notification deleted") }
    }

    private fun unreadNotificationConvert(notification: Slice<UnreadNotification>): Mono<NotificationResponseDTO> {
        val pageRequest: CassandraPageRequest = notification.pageable as CassandraPageRequest
        return Mono.just(NotificationResponseDTO(notification.content,null, pageRequest.pagingState.toString()))
    }

    private fun readNotificationConvert(notification: Slice<ReadNotification>): Mono<NotificationResponseDTO> {
        val pageRequest: CassandraPageRequest = notification.pageable as CassandraPageRequest
        return Mono.just(NotificationResponseDTO(null, notification.content, pageRequest.pagingState.toString()))
    }
}