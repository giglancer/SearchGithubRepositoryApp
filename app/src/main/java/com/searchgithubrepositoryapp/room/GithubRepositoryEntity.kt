package com.searchgithubrepositoryapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GithubRepositoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val repositoryName: String
)
