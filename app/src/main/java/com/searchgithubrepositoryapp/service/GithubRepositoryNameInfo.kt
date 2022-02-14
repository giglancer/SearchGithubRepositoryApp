package com.searchgithubrepositoryapp.service


data class GithubRepositoryNameInfo(
    val items: List<Item>,
    val total_count: Int
)

data class Item(
    val full_name: String,
)