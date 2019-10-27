package com.example.cassandra.CassandraDemo

data class NotificationResponseDTO(
        val notificationList: List<UnreadNotification>,
        val nextPage: String
)