package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import khalid.elnagar.notekeeper.entities.Note
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RetrieveNoteByPositionTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `RetrieveNoteByPosition with position then update result`() {
        val position = (0).toMutableLiveData()
        val result = MutableLiveData<Note?>()
        val repositoryMock = mock<NotesRepository> {
            on { retrieveNotes() } doReturn listOf(mock {})
        }
        val retrieveNoteByPosition = RetrieveNoteByPosition(position, result, repositoryMock)

        retrieveNoteByPosition()

        Assert.assertTrue(result.value != null)
    }

    @Test
    fun `RetrieveNoteByPosition with incorrect position then do nothing`() {
        val position = (1).toMutableLiveData()

        val result = MutableLiveData<Note?>().apply { value = null }
        val repositoryMock = mock<NotesRepository> {
            on { retrieveNotes() } doReturn listOf()
        }
        val retrieveNoteByPosition = RetrieveNoteByPosition(position, result, repositoryMock)

        retrieveNoteByPosition()

        Assert.assertTrue(result.value == null)
    }
}