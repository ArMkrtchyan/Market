package com.armboldmind.grandmarket.data.network.repositories

import android.content.Context
import com.armboldmind.grandmarket.data.IAddressRepository
import com.armboldmind.grandmarket.data.mappers.AddressMapper
import com.armboldmind.grandmarket.data.models.domain.Address
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.data.network.services.IAddressService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddressRepositoryRemote @Inject constructor(private val mAddressService: IAddressService, mApplication: Context) : BaseDataSource(mApplication), IAddressRepository {
    override suspend fun getAllAddresses(): Flow<List<Address>> {
        return getListResult(AddressMapper()) { mAddressService.getAllAddresses() }
    }

    override suspend fun addAddress(address: Address): Flow<Long> {
        return getResult { mAddressService.addAddress(address) }
    }

    override suspend fun updateAddress(address: Address): Flow<Boolean> {
        return getResult { mAddressService.updateAddress(address.id, address) }
    }

    override suspend fun deleteAddress(address: Address): Flow<Boolean> {
        return getResult { mAddressService.deleteAddress(address.id) }
    }

}