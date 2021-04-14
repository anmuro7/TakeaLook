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
class DeliveryGlassesFragmentViewModelTest {


    private lateinit var deliveryGlassesFragmentViewModel: DeliveryGlassesFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Mock
    lateinit var observerAdd: Observer<Boolean>

    @Mock
    lateinit var observerVerify: Observer<Boolean>

    @Mock
    lateinit var repository: Repository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        deliveryGlassesFragmentViewModel = DeliveryGlassesFragmentViewModel(repository)

        whenever( deliveryGlassesFragmentViewModel.addDonateToFirestore(mockVerifyCode,mockIdOptic,mockIdGlasses,mockPrice))
            .thenReturn(MutableLiveData(true))
        deliveryGlassesFragmentViewModel.addDonateToFirestore(mockVerifyCode,mockIdOptic,mockIdGlasses,mockPrice)
            .observeForever(observerAdd)

        whenever( deliveryGlassesFragmentViewModel.verifyReceptor(mockVerifyCode))
            .thenReturn(MutableLiveData(true))
        deliveryGlassesFragmentViewModel.verifyReceptor(mockVerifyCode).observeForever(observerVerify)

    }

    @Test
    fun addDonationToFirestoreSuccess() {
        val expected=true
        val captor = ArgumentCaptor.forClass(expected::class.java)
        captor.run {
            Mockito.verify(observerAdd, Mockito.times(1)).onChanged(capture())
            assertEquals(expected,value)
        }
    }

    @Test
    fun addDonationToFirestoreFailure() {
        val expected=false
        val captor = ArgumentCaptor.forClass(expected::class.java)
        captor.run {
            Mockito.verify(observerAdd, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expected,value)
        }
    }

    @Test
    fun verifyReceptorSuccess(){
        val expectedResult=true
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observerVerify, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult,value)
        }
    }

    @Test
    fun verifyReceptorFailure(){
        val expectedResult=false
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observerVerify, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expectedResult,value)
        }
    }

    private val mockVerifyCode:String="testCode"
    private val mockIdOptic:String="idoptictest"
    private val mockIdGlasses:String="idglassestest"
    private val mockPrice:Int=12

}