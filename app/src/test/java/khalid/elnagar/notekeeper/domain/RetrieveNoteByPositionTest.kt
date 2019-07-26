package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData

import khalid.elnagar.notekeeper.entities.Note
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RetrieveNoteByPositionTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dataGetWay by lazy { InMemoryDataGetWay }

    @Before
    fun setUp() {
        dataGetWay.restart()
    }

    @Test
    fun `RetrieveNoteByPosition with position then update result`() {
        //Arrange
        val position = (0).toMutableLiveData()
        val result = MutableLiveData<Note?>()
        val retrieveNoteByPosition = RetrieveNoteByPosition(position, result)

        //Act
        retrieveNoteByPosition()

        //Assert
        assertNotNull(result.value)
    }

    @Test
    fun `RetrieveNoteByPosition with incorrect position then do nothing`() {
        val position = New_Note.toMutableLiveData()

        val result = MutableLiveData<Note?>()

        val retrieveNoteByPosition = RetrieveNoteByPosition(position, result)

        retrieveNoteByPosition()

        assertNull(result.value)
    }
}