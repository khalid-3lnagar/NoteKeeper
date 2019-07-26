package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import khalid.elnagar.notekeeper.entities.Note
import khalid.elnagar.notekeeper.entities.NoteNoteFoundException
import org.junit.Assert.assertNotSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoveNoteByPositionTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dataGetWay by lazy { InMemoryDataGetWay }

    @Before
    fun setUp() {
        dataGetWay.restart()
    }

    @Test
    fun `RemoveNoteByPosition with valid position then remove  that Note`() {
        //Arrange
        val position = (1).toMutableLiveData()
        val oldValue = MutableLiveData<Note?>()
        val result = MutableLiveData<Note?>()
        val removeNoteByPosition = RemoveNoteByPosition(position)
        RetrieveNoteByPosition(position, oldValue)()

        //Act
        removeNoteByPosition()

        //Assert

        RetrieveNoteByPosition(position, result)()

        assertNotSame(result.value, oldValue.value)
    }

    @Test(expected = NoteNoteFoundException::class)
    fun `RemoveNoteByPosition with not valid position then throw Exception`() {
        //Arrange
        val position = (1).toMutableLiveData()
        val removeNoteByPosition = RemoveNoteByPosition(position)
        dataGetWay.clearNotes()

        //Act
        removeNoteByPosition()


    }

}