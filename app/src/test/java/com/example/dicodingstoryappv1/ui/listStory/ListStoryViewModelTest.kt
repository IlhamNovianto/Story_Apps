package com.example.dicodingstoryappv1.ui.listStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.dicodingstoryappv1.Adapter.StoryAdapter
import com.example.dicodingstoryappv1.DataDummy
import com.example.dicodingstoryappv1.MainCoroutineRule
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.ListStoryItem
import com.example.dicodingstoryappv1.api.response.StoryRepository
import com.example.dicodingstoryappv1.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
class ListStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutineRule()

    @Mock
    private lateinit var listStoryViewModel: ListStoryViewModel
    private val mockStoryRepository = Mockito.mock(StoryRepository::class.java)
    private val mockUserPreference = Mockito.mock(UserPreference::class.java)
    private val dummyToken = "jshdlakfjdshlfkjhluehfquiehf"
    private val dummySession = true

    @Before
    fun setUp(){
        listStoryViewModel = ListStoryViewModel(mockUserPreference, mockStoryRepository)
    }

    @Test
    fun `set logout successfully`() = runTest {
        listStoryViewModel.logout()
        Mockito.verify(mockUserPreference).logout()
    }

    @Test
    fun `get session login successfully`() {
        val expectedIslogin = flowOf(dummySession)
        Mockito.`when`(mockUserPreference.isLogin()).thenReturn(expectedIslogin)

        val actualIslogin = listStoryViewModel.isLogin().getOrAwaitValue()
        Mockito.verify(mockUserPreference).isLogin()
        Assert.assertNotNull(actualIslogin)
        Assert.assertEquals(dummySession, actualIslogin)
    }

    @Test
    fun `get token successfully`() {
        val expectedToken = flowOf(dummyToken)

        Mockito.`when`(mockUserPreference.getToken()).thenReturn(expectedToken)

        val actualToken = listStoryViewModel.getToken().getOrAwaitValue()

        Mockito.verify(mockUserPreference).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }


    @Test
    fun `when Get Story Should Not Null`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryListResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedReturn = MutableLiveData<PagingData<ListStoryItem>>()
        expectedReturn.value = data

        Mockito.`when`(mockStoryRepository.getStories(dummyToken)).thenReturn(expectedReturn)

        val actualReturn: PagingData<ListStoryItem> = listStoryViewModel.getStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualReturn)

        advanceUntilIdle()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
