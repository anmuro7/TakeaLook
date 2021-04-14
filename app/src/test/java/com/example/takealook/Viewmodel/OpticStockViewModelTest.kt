package com.example.takealook.Viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Donaciones
import com.example.takealook.data.model.Gafas
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

@RunWith(MockitoJUnitRunner::class)
class OpticStockViewModelTest {

    private lateinit var opticStockViewModel: OpticStockViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<List<Gafas>?>

    @Mock
    lateinit var repository: Repository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        opticStockViewModel = OpticStockViewModel(repository)


        whenever(opticStockViewModel.opticGlasses(mockOpticId)).thenReturn(MutableLiveData(mockGlassesList))
        opticStockViewModel.opticGlasses(mockOpticId).observeForever(observer)
    }


    @Test
    fun getGlassesForOpticTest(){
        val expectedResult=mockGlassesList
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertEquals(expectedResult[0].nombreDonante, value[0]?.nombreDonante)
            assertEquals(expectedResult[1].nombreDonante, value[1]?.nombreDonante)
        }
    }
    @Test
    fun getGlassesForOpticNotNullTest(){
        val expectedResult=mockGlassesList
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotNull(value)
        }
    }



    private val mockOpticId:String="mockOpticId"

    private val mockGlasses1 = Gafas(
        "Nombre1",
        "Apellido1 Apellido1.2",
        "00000000F",
        "666666666",
        "mockmail@mail.com",
        "jglkjjklhtkñhrljhf"
    )
    private val mockGlasses2 = Gafas(
        "Nombre2",
        "Apellido2 Apellido2.2",
        "00000000A",
        "666656666",
        "mockmail2@mail.com",
        "jglkjjklhkkgkrljhf"
    )

    private val mockGlassesList: List<Gafas> = arrayListOf(
        mockGlasses1, mockGlasses2
    )

}