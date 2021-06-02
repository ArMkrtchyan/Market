package com.armboldmind.grandmarket.data.mappers

interface IMapper<in T, out K> {
    fun map(data: T): K
}