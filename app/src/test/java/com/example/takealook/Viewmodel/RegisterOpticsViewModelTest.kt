package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.google.firebase.firestore.GeoPoint
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
class RegisterOpticsViewModelTest {
    private lateinit var registerOpticsViewModel: RegisterOpticsViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Boolean>

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        registerOpticsViewModel = RegisterOpticsViewModel(repository)

        whenever( registerOpticsViewModel.createNewUser(
            mockUser,mockPass,mockName,mockRazSoc,mockCif,
            mockAddress,mockCp,mockLocalidad,mockPhone,
            mockWeb,mockGeopoint
        )).thenReturn(MutableLiveData(true))
        registerOpticsViewModel.createNewUser(
            mockUser,mockPass,mockName,mockRazSoc,mockCif,
            mockAddress,mockCp,mockLocalidad,mockPhone,
            mockWeb,mockGeopoint).observeForever(observer)
    }

    @Test
    fun createNewUserTestSuccess(){
        val expectedResult=true
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult,value)
        }
    }
    @Test
    fun createNewUserTestFailure(){
        val expectedResult=false
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expectedResult,value)
        }
    }



    private val mockUser:String="mockUser@user.com"
    private val mockPass:String="mockPass"
    private val mockName:String="mockName"
    private val mockRazSoc:String="mockRazonSocial"
    private val mockCif:String="B12364578"
    private val mockAddress:String="mockAddress 456"
    private val mockCp:String="08023"
    private val mockLocalidad:String="mockLocalidad"
    private val mockPhone:String="933633636"
    private val mockWeb:String="www.mockweb.com"
    private val mockGeopoint:GeoPoint= GeoPoint(41.145,2.1856)

}