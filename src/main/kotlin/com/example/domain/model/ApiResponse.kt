package com.example.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ApiResponse(
    val success:Boolean
)