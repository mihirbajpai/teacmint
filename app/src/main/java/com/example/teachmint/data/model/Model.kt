package com.example.teachmint.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Repository(
    val id: Int,
    val name: String,
    val owner: Owner,
    val html_url: String,
    val description: String?,
    val contributors_url: String
)

data class Owner(
    val login: String,
    val avatar_url: String
)

data class ApiResponse(
    val items: List<Repository>
)


data class Contributor(
    val login: String,
    val avatar_url: String,
    val contributions: Int,
    val html_url: String
)

// For offline
@Entity(tableName = "repo_table")
data class LocalModel(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val login: String,
)