package com.kirson.googlebooks.repository


import android.util.Log
import com.kirson.googlebooks.MainRepository

import com.kirson.googlebooks.repository.dataSource.MainRemoteDataSource
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val mainRemoteDataSource: MainRemoteDataSource,
    //private val mainLocalDataSource: MainLocalDataSource
) : MainRepository {

//    override suspend fun getAllPhones(): PhonesNetworkModel? {
//
//        var phones: PhonesNetworkModel? = null
//
//        try {
//            val response = mainRemoteDataSource.getPhones()
//            val data = response.body()
//            if (data != null) {
//                phones = data
//            }
//        } catch (exception: Exception) {
//            Log.i("MyTag", exception.message.toString())
//        }
//
//
//
//        return phones
//    }
//
//
//    override suspend fun getPhoneDetails(): PhoneDetailNetworkModel? {
//
//        var details: PhoneDetailNetworkModel? = null
//
//        try {
//            val response = mainRemoteDataSource.getPhoneDetails()
//            val data = response.body()
//            if (data != null) {
//                details = data
//            }
//
//        } catch (exception: Exception) {
//            Log.i("MyTag", exception.message.toString())
//        }
//
//
//        return details
//    }



}


