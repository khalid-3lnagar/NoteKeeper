package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import khalid.elnagar.notekeeper.entities.Note
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RetrieveAllNotesTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `RetrieveAllNotesTest with empty response then do nothing`() {
        //Arrange
        val repositoryMock = mock<NotesRepository> {
            on { retrieveNotes() } doReturn listOf()
        }
        val result = listOf<Note>().toMutableLiveData()
        val retrieveAllNotes = RetrieveAllNotes(result, repositoryMock)
        //Act
        retrieveAllNotes()

        //Assert
        Assert.assertTrue(result.value.isNullOrEmpty())
    }

    @Test
    fun `RetrieveAllNotesTest with response then Update UiLiveData`() {
        //Arrange
        val noteMock = mock<Note> {}
        val repositoryMock = mock<NotesRepository> {
            on { retrieveNotes() } doReturn listOf(noteMock)
        }
        val result = listOf<Note>().toMutableLiveData()
        val retrieveAllCourses = RetrieveAllNotes(result, repositoryMock)
        //Act
        retrieveAllCourses()

        //Assert

        Assert.assertTrue(result.value?.isNotEmpty() ?: false)
    }
}