package com.kirson.googlebooks.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BooksAPITest {
    private lateinit var service: BooksAPIService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksAPIService::class.java)
    }

    private fun enqueueMockResponse(
        fileName: String
    ) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getAdventureBooks_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("adventure_books_response.json")
            val responseBody = service.getBooks("+subject:Adventure", 0).body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/books/v1/volumes?q=%2Bsubject%3AAdventure&startIndex=0&key=AIzaSyBnjJUBSkRuln1zFvWafKZFK2rUnTCvpW8")
        }
    }


    @Test
    fun getAdventureBooks_receivedResponse_correctPageSize() {
        runBlocking {
            enqueueMockResponse("adventure_books_response.json")
            val responseBody = service.getBooks("+subject:Adventure", 0).body()
            val articlesList = responseBody!!.books
            assertThat(articlesList?.size).isEqualTo(10)
        }
    }

    @Test
    fun getAdventureBooks_receivedResponse_correctContent() {
        runBlocking {
            enqueueMockResponse("adventure_books_response.json")
            val responseBody = service.getBooks("+subject:Adventure", 0).body()
            val bookList = responseBody!!.books
            val book = bookList!![0]
            assertThat(book.bookInfoNetworkModel.title).isEqualTo("The Books of the South: Tales of the Black Company")
            assertThat(book.bookInfoNetworkModel.publishedDate).isEqualTo("2008-06-10")
            assertThat(book.bookInfoNetworkModel.language).isEqualTo("en")
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}