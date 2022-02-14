package com.searchgithubrepositoryapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GithubRepositoryEntity::class], version = 1)
abstract class GithubRepositoryDatabase: RoomDatabase() {
    abstract fun githubRepositoryDAO(): GithubRepositoryDAO
}