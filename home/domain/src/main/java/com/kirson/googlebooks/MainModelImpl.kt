package com.kirson.googlebooks

import com.kirson.googlebooks.core.entity.mapDistinctNotNullChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

class MainModelImpl(
    private val repository: MainRepository,
) : MainModel {

    private val stateFlow = MutableStateFlow(State())

    data class State(
        val s: String?=null
//        val allPhones: PhonesDomainModel? = null,
//        val phoneDetails: PhoneDetailDomainModel? = null,
//        val cart: CartDomainModel? = null
    )


//    override val allPhones: Flow<PhonesDomainModel>
//        get() = stateFlow.mapDistinctNotNullChanges {
//            it.allPhones
//        }.flowOn(Dispatchers.IO)
//    override val phoneDetails: Flow<PhoneDetailDomainModel>
//        get() = stateFlow.mapDistinctNotNullChanges {
//            it.phoneDetails
//        }.flowOn(Dispatchers.IO)
//
//
//    override suspend fun getAllPhones(): PhonesDomainModel? {
//
//        var phones: PhonesDomainModel? = null
//
//        val data = repository.getAllPhones()
//
//        if (data != null) {
//            phones = data.toDomainModel()
//        }
//
//        stateFlow.update { state ->
//            state.copy(
//                allPhones = phones
//            )
//        }
//
//        return phones
//    }
//
//
//    override suspend fun getPhoneDetails(): PhoneDetailDomainModel? {
//
//        var details: PhoneDetailDomainModel? = null
//
//        val data = repository.getPhoneDetails()
//
//        if (data != null) {
//            details = data.toDomainModel()
//        }
//
//        stateFlow.update { state ->
//            state.copy(
//                phoneDetails = details
//            )
//        }
//
//        return details
//    }


}