package com.example.cassandra.CassandraDemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CassandraDemoApplication

fun main(args: Array<String>) {
	runApplication<CassandraDemoApplication>(*args)
}
