package com.example.cassandra.CassandraDemo

import com.datastax.driver.core.querybuilder.QueryBuilder
import org.springframework.data.cassandra.core.ReactiveCassandraOperations
import org.springframework.data.cassandra.core.query.CassandraPageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.Operators.`as`
import com.datastax.driver.core.querybuilder.QueryBuilder.column



@Component
class ReactiveNotificationAdvancedRepositoryImpl(private val operations: ReactiveCassandraOperations): ReactiveNotificationAdvancedRepository  {

    override fun findAllUnreadNotificationByUserId(userId: String, page: Pageable): Mono<Slice<UnreadNotification>> {
//Need to create following materialized view:
//                CREATE MATERIALIZED VIEW unread_notifications_by_date AS
//                SELECT * FROM unreadnotification WHERE userid IS NOT NULL AND id IS NOT NULL AND createdate IS NOT NULL
//                PRIMARY KEY ((userid), createdate, id)
//                WITH CLUSTERING ORDER BY (createdate desc);
        val pageRequest: CassandraPageRequest = page as CassandraPageRequest
        val qb = QueryBuilder.select()
                .from("unread_notifications_by_date")
                .where(QueryBuilder.eq("userId", userId))
                .orderBy(QueryBuilder.desc("createdate"))
                .setPagingState(pageRequest.pagingState)
                .setFetchSize(pageRequest.pageSize)
        return operations.slice(qb, UnreadNotification::class.java)
    }

    override fun findAllReadNotificationByUserId(userId: String, page: Pageable): Mono<Slice<ReadNotification>> {
//Need to create following materialized view:
//                CREATE MATERIALIZED VIEW read_notifications_by_date AS
//                SELECT * FROM readnotification WHERE userid IS NOT NULL AND id IS NOT NULL AND createdate IS NOT NULL
//                PRIMARY KEY ((userid), createdate, id)
//                WITH CLUSTERING ORDER BY (createdate desc);
        val pageRequest: CassandraPageRequest = page as CassandraPageRequest
        val qb = QueryBuilder.select()
                .from("read_notifications_by_date")
                .where(QueryBuilder.eq("userId", userId))
                .orderBy(QueryBuilder.desc("createdate"))
                .setPagingState(pageRequest.pagingState)
                .setFetchSize(pageRequest.pageSize)
        return operations.slice(qb, ReadNotification::class.java)
    }

    override fun countUnreadNotificationByUserId(userId: String): Mono<NotificationCount> {
        val qb = QueryBuilder.select()
                .count("userId").`as`("notificationAmount")
                .column("createDate").`as`("lastNotificationDateTime")
                .from("unread_notifications_by_date")
                .where(QueryBuilder.eq("userId", userId))
                .limit(1)
        return operations.selectOne(qb, NotificationCount::class.java)
    }

}
