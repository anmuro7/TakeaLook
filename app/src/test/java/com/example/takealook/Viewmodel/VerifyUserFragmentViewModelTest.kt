package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.google.common.base.Verify
import com.nhaarman.mockitokotlin2.mock
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
class VerifyUserFragmentViewModelTest {
    private lateinit var verifyUserFragmentViewModel: VerifyUserFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Boolean>

    @Mock
    lateinit var repository: Repository
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        verifyUserFragmentViewModel= VerifyUserFragmentViewModel(repository)
        whenever( verifyUserFragmentViewModel.verifyReceptor(mockCodeUser)).thenReturn(MutableLiveData(true))
        verifyUserFragmentViewModel.verifyReceptor(mockCodeUser).observeForever(observer)
    }

    @Test
    fun verifyReceptorTestSuccess(){
        val expectedResult=true
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult,value)
        }
    }
    @Test
    fun verifyReceptorTestFailure(){
        val expectedResult=false
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expectedResult,value)
        }
    }


    private val mockCodeUser:String="jghlkfdjhlñfkdhhf"
}