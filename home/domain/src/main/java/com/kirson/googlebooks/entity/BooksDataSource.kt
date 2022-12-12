package com.kirson.googlebooks.entity

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kirson.googlebooks.HomeModel

const val pageSize = 10

class BooksDataSource(
    private val homeModel: HomeModel
) : PagingSource<Int, BookDomainModel>() {

    override fun getRefreshKey(state: PagingState<Int, BookDomainModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookDomainModel> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response = homeModel.loadBooksByIndex(nextPageNumber * pageSize)


            LoadResult.Page(
                data = response?.books!!,
                prevKey = null,
                nextKey = if (response.totalItems > nextPageNumber * pageSize) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}