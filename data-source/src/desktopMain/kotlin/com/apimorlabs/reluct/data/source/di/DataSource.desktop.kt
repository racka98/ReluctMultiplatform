package com.apimorlabs.reluct.data.source.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.data.source.database.ReluctDatabase
import work.racka.reluct.data.source.database.adapters.GoalsTableAdapter
import work.racka.reluct.data.source.database.adapters.TasksTableAdapter
import work.racka.reluct.data.source.database.dao.DatabaseWrapper
import work.racka.reluct.data.source.util.Constants
import java.io.File

internal actual fun platformModule(): Module = module {
    single<DatabaseWrapper> {
        val dbPath = File(System.getProperty("java.io.tmpdir"), Constants.RELUCT_DATABASE)
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${dbPath.absolutePath}")
            .also { ReluctDatabase.Schema.create(it) }
        DatabaseWrapper(
            instance = ReluctDatabase(
                driver = driver,
                GoalsTableAdapter = GoalsTableAdapter,
                TasksTableAdapter = TasksTableAdapter
            )
        )
    }
}