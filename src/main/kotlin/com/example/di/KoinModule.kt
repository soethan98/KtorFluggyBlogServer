package com.example.di

import com.example.data.repository.UserDataSourceImpl
import com.example.domain.repository.UserDataSource
import com.example.utils.Constants.DATABASE_NAME
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.config.ConfigLoader.Companion.load
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    }

    single {
        KMongo.createClient(connectionString = System.getenv("MONGO_CONNECTION")
        )
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

    single <UserDataSource>{ UserDataSourceImpl(get()) }
}


