package com.example.dicodingstoryappv1.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.dicodingstoryappv1.DataDummy
import com.example.dicodingstoryappv1.MainCoroutineRule
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.GetAllStoryResponse
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.api.response.StoryRepository
import com.example.dicodingstoryappv1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private val mockUserPreference = Mockito.mock(UserPreference::class.java)
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyToken = "jshdlakfjdshlfkjhluehfquiehf"
    private var dummyMaps = DataDummy.generateDummyMapsMarker()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository, mockUserPreference)
    }

    @Test
    fun `when get Stories is Called Should Not Null and Return Success`() {
        val expectedMarker = MutableLiveData<Result<GetAllStoryResponse>>()
        expectedMarker.value = Result.Success(dummyMaps)
        Mockito.`when`(storyRepository.getStoryMaps(dummyToken, 1)).thenReturn(expectedMarker)

        val actualStory = mapsViewModel.getStoryMaps(dummyToken, 1).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryMaps(dummyToken, 1)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success<*>)
        Assert.assertEquals(dummyMaps, (actualStory as Result.Success<*>).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedStory = MutableLiveData<Result<GetAllStoryResponse>>()
        expectedStory.value = Result.Error("Error")

        Mockito.`when`(storyRepository.getStoryMaps(dummyToken, 1)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryMaps(dummyToken,1).getOrAwaitValue()

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }

    @Test
    fun `getToken success`() {
        val expectedToken = flowOf(dummyToken)
        Mockito.`when`(mockUserPreference.getToken()).thenReturn(expectedToken)

        val actualToken = mapsViewModel.getToken().getOrAwaitValue()

        Mockito.verify(mockUserPreference).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }
}