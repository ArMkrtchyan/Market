package com.armboldmind.grandmarket.data.models.paginatonModels

class PaginationResponseModel<T> {
    val content: List<T> = arrayListOf()
    val totalElements: Int = 0
    val totalPages: Int = 0
    val last: Boolean = true
    val number: Int = 0
    val size: Int = 0
    val numberOfElements: Int = 0
    val first: Boolean = true
    val empty: Boolean = true
}