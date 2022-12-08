package com.kirson.googlebooks.repository.dataSourceImpl

import com.kirson.googlebooks.api.BooksAPIService
import com.kirson.googlebooks.repository.dataSource.MainRemoteDataSource
import javax.inject.Inject

class MainRemoteDataSourceImpl @Inject constructor(
    private val booksAPIService: BooksAPIService
) : MainRemoteDataSource {
//    override suspend fun getPhones(): Response<PhonesNetworkModel> =
//        mainAPIService.getPhones()
//
//
//    override suspend fun getPhoneDetails(): Response<PhoneDetailNetworkModel> =
//        mainAPIService.getPhoneDetails()
//


}