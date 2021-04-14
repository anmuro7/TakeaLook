package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Donaciones
import com.example.takealook.data.model.Optica
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
import java.util.*
@RunWith(MockitoJUnitRunner::class)
class DonationsOpticFragmentViewModelTest {
    private lateinit var donationsOpticFragmentViewModel: DonationsOpticFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock lateinit var observer: Observer<List<Donaciones>?>

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        donationsOpticFragmentViewModel = DonationsOpticFragmentViewModel(repository)
        whenever( donationsOpticFragmentViewModel.currentUser()).thenReturn(mockCurrentUser)

        whenever(donationsOpticFragmentViewModel.getOpticDonations(mockIdOptic!!))
            .thenReturn(MutableLiveData(mockDonationsList))
        donationsOpticFragmentViewModel.getOpticDonations(mockIdOptic).observeForever(observer)

    }

    @Test
    fun loadDonationsOptic(){
        val expectedResult = mockDonationsList
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult[0].nombreDonante, value[0]?.nombreDonante)
            assertEquals(expectedResult[1].nombreDonante, value[1]?.nombreDonante)
        }
    }


    @Test
    fun loadDonationsNotNull(){
        val expectedResult = mockDonationsList
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotNull(value)
        }
    }

    @Test
    fun currentUserIsNull(){
        assertNull(donationsOpticFragmentViewModel.currentUser(),mockCurrentUserNull)
    }

    @Test
    fun currentUserNotNull(){
        assertNotNull(donationsOpticFragmentViewModel.currentUser(),mockCurrentUser)
    }


    private val mockCurrentUser:String?="currentUser"
    private val mockCurrentUserNull:String?=null
    private val mockIdOptic:String?="mockidoptic"

    private val mockDonation1:Donaciones=Donaciones(
        "mockDonante1",
        "mockApellidosDonante1",
        "mockReceptor1",
        "mockApellidosReceptor1",
        "654789321",
        "123456789",
        "mockdonante1@mail.com",
        "mockreceptor1@mail.com",
        "mockidglasses1",
        "02/02/2020",
        15,
        "mockDonation1"
    )
    private val mockDonation2:Donaciones=Donaciones(
        "mockDonante2",
        "mockApellidosDonante2",
        "mockReceptor2",
        "mockApellidosReceptor2",
        "654789251",
        "123456452",
        "mockdonante2@mail.com",
        "mockreceptor2@mail.com",
        "mockidglasses2",
        "02/03/2020",
        11,
        "mockDonation2"
    )

        private val mockDonationsList: List<Donaciones> = arrayListOf(
        mockDonation1, mockDonation2
    )
}