package khalid.elnagar.notekeeper.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import khalid.elnagar.notekeeper.entities.Note
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveNoteByPositionTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dataGetWay by lazy { InMemoryDataGetWay }

    @Before
    fun setUp() {
        dataGetWay.restart()
    }

    @Test
    fun `SaveNoteByPositionTest with valid position then update result`() {
        //Arrange
        val noteMock = mock<Note> {}
        val result = MutableLiveData<Note?>()
        val position = (3).toMutableLiveData()
        val saveNoteByPosition = SaveNoteByPosition(position)

        //Act
        saveNoteByPosition(noteMock)
        RetrieveNoteByPosition(position, result)()

        Assert.assertSame(result.value, noteMock)


    }
}