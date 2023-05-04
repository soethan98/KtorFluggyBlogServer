package com.example.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ApiResponse<T>(
    val success:Boolean,
    val data:T?,
    val message:String? = null
)