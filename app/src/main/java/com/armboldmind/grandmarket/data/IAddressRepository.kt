package com.armboldmind.grandmarket.data

import com.armboldmind.grandmarket.data.models.domain.Address
import kotlinx.coroutines.flow.Flow

interface IAddressRepository {
    suspend fun getAllAddresses(): Flow<List<Address>>
    suspend fun addAddress(address: Address): Flow<Long>
    suspend fun updateAddress(address: Address): Flow<Boolean>
    suspend fun deleteAddress(address: Address): Flow<Boolean>
}