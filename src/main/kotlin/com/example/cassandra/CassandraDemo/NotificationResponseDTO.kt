package com.example.cassandra.CassandraDemo

data class NotificationResponseDTO(
        val unreadNotificationList: List<UnreadNotification>?,
        val readNotificationList: List<ReadNotification>?,
        val nextPage: String
)