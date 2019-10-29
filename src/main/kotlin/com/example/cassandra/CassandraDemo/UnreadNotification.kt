package com.example.cassandra.CassandraDemo

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Indexed
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.*

@Table
data class UnreadNotification(
        @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        val id: UUID? = UUID.randomUUID(),
        @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        val userId: String,
        @Column
        val createDate: Date? = Date(),
        @Column
        val notificationType: NotificationType,
        @Column
        val notificationFunction: NotificationFunction,
        @Column
        val messageHeader: String,
        @Column
        val updtUserId: String,
        @Column
        val updtCompanyId: String,
        @Column
        val recordCount: Long,
        @Column
        val recordDate: Date,
        @Column
        val readSw: String = "N"
)