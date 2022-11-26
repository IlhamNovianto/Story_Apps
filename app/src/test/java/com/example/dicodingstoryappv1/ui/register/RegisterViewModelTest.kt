package com.example.dicodingstoryappv1.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.dicodingstoryappv1.DataDummy
import com.example.dicodingstoryappv1.Preference.UserPreference
import com.example.dicodingstoryappv1.api.response.RegisterResponse
import com.example.dicodingstoryappv1.api.response.Result
import com.example.dicodingstoryappv1.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest{
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userPreference: UserPreference
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyResponse = DataDummy.generateDummyRegisterReponse()
    private val dummynama = "dico"
    private val dummyemail = "dico123@gmail.com"
    private val dummypassword = "123456"


    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userPreference)
    }

    @Test
    fun `when register() is Called Should Not Null and Return Success`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(dummyResponse)
        Mockito.`when`(userPreference.register(dummynama,dummyemail,dummypassword)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register(dummynama,dummyemail,dummypassword).getOrAwaitValue()

        Mockito.verify(userPreference).register(dummynama,dummyemail,dummypassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(dummyResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Error("Error")
        Mockito.`when`(userPreference.register(dummynama,dummyemail,dummypassword)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register(dummynama,dummyemail,dummypassword).getOrAwaitValue()

        Mockito.verify(userPreference).register(dummynama,dummyemail,dummypassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}