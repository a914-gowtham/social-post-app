package com.compose.template.viewmodel

import com.compose.template.MainCoroutinesRule
import com.compose.template.MockUtil
import com.compose.template.data.ApiService
import com.compose.template.data.PostDataSourceImpl
import com.compose.template.presentation.MainViewModel
import com.compose.template.usecases.GetPostUseCase
import com.compose.template.utils.ApiState
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.*
import retrofit2.Response

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var getPostUseCase: GetPostUseCase

    private val apiService: ApiService = mock()

    private val dataSourceImpl = PostDataSourceImpl(apiService)

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        getPostUseCase = GetPostUseCase(dataSourceImpl)
        viewModel = MainViewModel(getPostUseCase)
    }

    @Test
    fun fetchAllPost() = runBlocking {
        val mockData = MockUtil.mockPostList()
        whenever(apiService.getPokemonList()).thenReturn(
            Response.success(200,mockData))

        getPostUseCase.execute().apply {
            assertThat(this).isInstanceOf(ApiState.Success::class.java)
        }

        viewModel.fetchAllPost()
        verify(apiService, atLeastOnce()).getPokemonList()
        Unit //JUnit test should return Unit
    }



}