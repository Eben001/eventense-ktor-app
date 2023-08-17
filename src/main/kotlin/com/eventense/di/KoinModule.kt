package com.eventense.di

import com.eventense.data.repository.UserDataSourceImpl
import com.eventense.domain.repository.UserDataSource
import com.eventense.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        KMongo.createClient(System.getenv("MONGO_URL"))
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

    single<UserDataSource>{
        UserDataSourceImpl(get())
    }

}