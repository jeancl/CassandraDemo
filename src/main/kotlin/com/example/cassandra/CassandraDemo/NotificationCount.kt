package com.example.cassandra.CassandraDemo

import java.util.*

data class NotificationCount(
        val notificationAmount: Long,
        val lastNotificationDateTime: Date
)