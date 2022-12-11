package com.example.dicodingstoryappv1.ui.login

import android.text.BoringLayout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.dicodingstoryappv1.DataDummy
import com.example.dicodingstoryappv1.MainCoroutineRule
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.LoginResponse
import com.example.dicodingstoryappv1.api.response.LoginResult
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.OngoingStubbing

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var userPreference: UserPreference
    private lateinit var loginViewModel: LoginViewModel
    private val dummyResponse = DataDummy.generateDummyLoginResponseSuccess()
    private val dummyResult = DataDummy.generateDummyLoginResult()
    private val dummyEmail = "dico@gmail.com"
    private val dummyPass = "123456"
    private val dummyToken = "lkshalkjdhfdf"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userPreference)
    }
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when setToken call state true`() = runTest{
        loginViewModel.setToken(dummyResult.token, true)
        Mockito.verify(userPreference).setState(dummyResult.token, true)
    }

    @Test
    fun `when get token not null`()  {
        val expectedToken = flowOf(dummyToken)
        `when`(userPreference.getToken()).thenReturn(expectedToken)
        val actualToken = loginViewModel.getToken().getOrAwaitValue()
        Mockito.verify(userPreference).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }

    @Test
    fun `when login() is Called Should Return Success and Data`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(dummyResponse)
        `when`(userPreference.login(dummyEmail, dummyPass)).thenReturn(expectedResponse)

        val actualResponse =
            loginViewModel.login(dummyEmail, dummyPass).getOrAwaitValue()

        Mockito.verify(userPreference).login(dummyEmail, dummyPass)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(dummyResponse, (actualResponse as Result.Success<*>).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Error("Error")
        `when`(userPreference.login(dummyEmail, dummyPass)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(dummyEmail, dummyPass).getOrAwaitValue()

        Mockito.verify(userPreference).login(dummyEmail, dummyPass)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }


}
