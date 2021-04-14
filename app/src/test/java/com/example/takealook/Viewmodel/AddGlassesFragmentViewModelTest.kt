package com.example.takealook.Viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.takealook.data.Repository
import com.example.takealook.data.model.Gafas
import com.example.takealook.data.model.Optica
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddGlassesFragmentViewModelTest {
    private lateinit var addGlassesFragmentViewModel: AddGlassesFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Boolean>

    @Mock
    lateinit var repository: Repository




    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        addGlassesFragmentViewModel = AddGlassesFragmentViewModel(repository)
        whenever( addGlassesFragmentViewModel.addDataOfGlasses(mockGlasses,mockBitmap)).thenReturn(MutableLiveData(true))
        addGlassesFragmentViewModel.addDataOfGlasses(mockGlasses,mockBitmap).observeForever(observer)
    }


    @Test
    fun addDataOfGlassesSuccess() {
        val expectedResult = true
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertTrue(value)
        }
    }

    @Test
    fun addDataOfGlassesFailed() {
        val expectedResult = false
        val captor = ArgumentCaptor.forClass(expectedResult::class.java)
        captor.run {
            Mockito.verify(observer, Mockito.times(1)).onChanged(capture())
            assertNotEquals(expectedResult,value)
        }
    }

    private val mockGlasses = Gafas(
        "Nombre",
        "Apellido1 Apellido2",
        "00000000F",
        "666666666",
        "mockmail@mail.com",
        "jglkjjklhtkñhrljhf"
    )
     private var mockBitmap: Bitmap = mock(Bitmap::class.java)


}