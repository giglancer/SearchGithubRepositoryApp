package com.searchgithubrepositoryapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert

@Dao
interface GithubRepositoryDAO {
    @Insert
    fun insert(githubRepositoryEntity: GithubRepositoryEntity)

    @androidx.room.Query("select * from githubRepositoryEntity")
    fun getAll(): LiveData<List<GithubRepositoryEntity>>

    @androidx.room.Query("delete from githubRepositoryEntity")
    fun deleteAll()
}