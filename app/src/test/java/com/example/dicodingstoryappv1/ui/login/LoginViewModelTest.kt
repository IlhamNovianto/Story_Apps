package com.example.dicodingstoryappv1.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.dicodingstoryappv1.DataDummy
import com.example.dicodingstoryappv1.MainCoroutineRule
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.LoginResponse
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Mock
    private lateinit var userPreference: UserPreference
    private lateinit var loginViewModel: LoginViewModel
    private val dummyResponse = DataDummy.generateDummyLoginResponseSuccess()
    private val dummyEmail = "dico@gmail.com"
    private val dummyPass = "123456"
    private val dummyToken = "jshdlakfjdshlfkjhluehfquiehf"


    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userPreference)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when set token`()= runTest {
        loginViewModel.setToken(dummyToken, true)
        Mockito.verify(userPreference).setToken(dummyToken, true)
    }

    @Test
    fun `when get token Success`()  {
        val expectedToken = flowOf(dummyToken)
        Mockito.`when`(userPreference.getToken()).thenReturn(expectedToken)

        val actualToken = loginViewModel.getToken().getOrAwaitValue()

        Mockito.verify(userPreference).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }


    @Test
    fun `when login() is Called Should Return Success and Data`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(dummyResponse)
        Mockito.`when`(userPreference.login(dummyEmail, dummyPass)).thenReturn(expectedResponse)

        val actualResponse =
            loginViewModel.login(dummyEmail, dummyPass ).getOrAwaitValue()

        Mockito.verify(userPreference).login(dummyEmail, dummyPass)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(true)
        Assert.assertEquals(dummyResponse, (actualResponse as Result.Success<*>).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Error("Error")
        Mockito.`when`(userPreference.login(dummyEmail, dummyPass)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(dummyEmail, dummyPass).getOrAwaitValue()

        Mockito.verify(userPreference).login(dummyEmail, dummyPass)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }


}