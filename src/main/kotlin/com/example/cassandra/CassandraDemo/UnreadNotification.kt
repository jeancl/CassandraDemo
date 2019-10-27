package com.example.cassandra.CassandraDemo

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.*

@Table
data class UnreadNotification(
        @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        val id: UUID? = UUID.randomUUID(),
        @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        val userId: String,
        @PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        val createDate: Date? = Date(),
        @Column
        val operation: Operation,
        @Column
        val type: Type,
        @Column
        val updateUserId: String,
        @Column
        val amountOfRecords: Long,
        @Column
        val readSw: String = "N"
)