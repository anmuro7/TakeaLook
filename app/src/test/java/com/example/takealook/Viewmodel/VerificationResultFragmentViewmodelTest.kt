package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Receptores
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
class VerificationResultFragmentViewmodelTest {
    private lateinit var verificationResultFragmentViewmodel: VerificationResultFragmentViewmodel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Receptores>

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        verificationResultFragmentViewmodel= VerificationResultFragmentViewmodel(repository)
        whenever( verificationResultFragmentViewmodel.getReceptorData(mockCodeUser)).thenReturn(MutableLiveData(mockReceptor))
        verificationResultFragmentViewmodel.getReceptorData(mockCodeUser).observeForever(observer)

    }

    @Test
    fun getReceptorDataTestSuccess(){
        val expectedResult=mockReceptor
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult.nombre,value.nombre)
        }
    }
    @Test
    fun getReceptorDataTestFailure(){
        val expectedResult=mockReceptor
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expectedResult.nombre,value.nombre+"4")
        }
    }
    @Test
    fun loadOpticsTestNotNull(){
        val expectedResult = mockReceptor
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotNull(expectedResult)
        }
    }


    private val mockReceptor:Receptores=Receptores(
        "mockName",
        "mocksurname1 mockSurname2",
        "41563214H",
        "936547898",
        "mockAddress 12",
        "08012",
        "Barcleona",
        "mockmail@mail.com"
    )

    private val mockCodeUser:String="hhjjgjgjdkfgog64565ddsf"
}