package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RecoverPasswordViewModelTest {
    private lateinit var recoverPasswordViewModel: RecoverPasswordViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Boolean>

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        recoverPasswordViewModel = RecoverPasswordViewModel(repository)
        whenever( recoverPasswordViewModel.recoverPass(mockEmailUser)).thenReturn(MutableLiveData(true))
        recoverPasswordViewModel.recoverPass(mockEmailUser).observeForever(observer)
    }

    @Test
    fun recoverPassSuccess(){
        val expectedResult=true
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult,value)
        }
    }

    @Test
    fun recoverPassfailure(){
        val expectedResult=false
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expectedResult,value)
        }
    }

    private val mockEmailUser:String="mockEmail@email.com"
}