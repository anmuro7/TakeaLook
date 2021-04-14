package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Optica
import com.example.takealook.ui.ui.RegisterOpticsActivity
import com.google.firebase.firestore.GeoPoint
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MapaOpticasViewModelTest {

    private lateinit var mapaOpticasViewModel: MapaOpticasViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock lateinit var observer: Observer<List<Optica>?>

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mapaOpticasViewModel = MapaOpticasViewModel(repository)
        whenever(mapaOpticasViewModel.loadOptics()).thenReturn(MutableLiveData(mockOpticList))
        mapaOpticasViewModel.loadOptics().observeForever(observer)
    }

    @Test
    fun loadOpticsTest() {
        val expectedResult = mockOpticList
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult[0].Nombre, value[0]?.Nombre)
            assertEquals(expectedResult[1].Nombre, value[1]?.Nombre)
        }
    }
    @Test
    fun loadOpticsTestNotNull(){
        val expectedResult = mockOpticList
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotNull(value)
        }
    }



    private   val mockOptic = Optica(
            "Optica1",
            "B45658985",
            "Optica1 S.L.",
            "direccion 1",
            "00001",
            "Localidad 1",
            "933003336",
            "optica1@optica.es",
            "www.optica1.com",
            false,
            GeoPoint(41.0010,05.0223),
            "optica1"
        )

        private val mockOptic2 = Optica(
            "Optica2",
            "B45658925",
            "Optica2 S.L.",
            "direccion 2",
            "00002",
            "Localidad 2",
            "933003335",
            "optica2@optica.es",
            "www.optica2.com",
            false,
            GeoPoint(42.0010,03.0223),
            "optica1"
        )

        private val mockOpticList: List<Optica> = arrayListOf(
            mockOptic, mockOptic2
        )
    }

