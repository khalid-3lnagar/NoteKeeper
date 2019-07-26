package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import khalid.elnagar.notekeeper.entities.Note
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RetrieveAllNotesTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dataGetWay by lazy { InMemoryDataGetWay }

    @Before
    fun setUp() {
        dataGetWay.restart()
    }

    @Test
    fun `RetrieveAllNotesTest with empty response then do nothing`() {
        //Arrange
        val result = listOf<Note>().toMutableLiveData()
        val retrieveAllNotes = RetrieveAllNotes(result)
        dataGetWay.clearNotes()
        //Act
        retrieveAllNotes()

        //Assert
        Assert.assertTrue(result.value.isNullOrEmpty())
    }

    @Test
    fun `RetrieveAllNotesTest with response then Update UiLiveData`() {
        //Arrange

        val result = listOf<Note>().toMutableLiveData()
        val retrieveAllCourses = RetrieveAllNotes(result)

        //Act
        retrieveAllCourses()

        //Assert

        Assert.assertTrue(result.value?.isNotEmpty() ?: false)
    }
}