package com.example.dicodingstoryappv1.ui.OnCamera

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.dicodingstoryappv1.DataDummy
import com.example.dicodingstoryappv1.MainCoroutineRule
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.AddNewStoryResponse
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.api.response.StoryRepository
import com.example.dicodingstoryappv1.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainCoroutineRule()

    @Mock
    private lateinit var addStoryViewModel: AddStoryViewModel

    private val mockStoryRepository = Mockito.mock(StoryRepository::class.java)
    private val mockUserPreference = Mockito.mock(UserPreference::class.java)

    private var dummyMultipart = DataDummy.generateDummyMultipartFile()
    private var dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyResponse = DataDummy.generateDummyStoryyResponseSuccess()
    private val dummyToken = "jshdlakfjdshlfkjhluehfquiehf"

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(mockStoryRepository, mockUserPreference)
    }

    @Test
    fun `get token success`() {
        val expectationToken = flowOf(dummyToken)
        Mockito.`when`(mockUserPreference.getToken()).thenReturn(expectationToken)

        val actualToken = addStoryViewModel.getToken().getOrAwaitValue()

        Mockito.verify(mockUserPreference).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }

    @Test
    fun `when add story is called Should Not Null and Return Success`() {
        val expectdeResponse = MutableLiveData<Result<AddNewStoryResponse>>()
        expectdeResponse.value = Result.Success(dummyResponse)

        Mockito.`when`(mockStoryRepository.addStories
            ("token",
            dummyMultipart,
            dummyDescription,
            "11.232",
            "11.232")
        ).thenReturn(expectdeResponse)

        val actualUplload =
            addStoryViewModel.addStory(
                "token",
                dummyMultipart,
                dummyDescription,
                "11.232",
                "11.232"
            ).getOrAwaitValue()

        Mockito.verify(mockStoryRepository).addStories(
            "token",
            dummyMultipart,
            dummyDescription,
            "11.232",
            "11.232")
        Assert.assertNotNull(actualUplload)
        Assert.assertTrue(actualUplload is Result.Success)
        Assert.assertEquals(dummyResponse, (actualUplload as Result.Success).data)
    }

    @Test
    fun `when add story Error Should Return Error`() {
        val expectdeUplload = MutableLiveData<Result<AddNewStoryResponse>>()
        expectdeUplload.value = Result.Error("ERROR")
        Mockito.`when`(mockStoryRepository.addStories(
            "token",
            dummyMultipart,
            dummyDescription,
            "11.232",
            "11.232"))
            .thenReturn(
                expectdeUplload
            )
        val actualUplload = addStoryViewModel.addStory(
                "token",
                dummyMultipart,
                dummyDescription,
                "11.232",
                "11.232").getOrAwaitValue()

        Mockito.verify(mockStoryRepository).addStories(
            "token",
            dummyMultipart,
            dummyDescription,
            "11.232",
            "11.232")
        Assert.assertNotNull(actualUplload)
        Assert.assertTrue(actualUplload is Result.Error)

    }
}