package khalid.elnagar.notekeeper.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import khalid.elnagar.notekeeper.entities.Course
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RetrieveAllCoursesTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dataGetWay by lazy { InMemoryDataGetWay }

    @Before
    fun setUp() {
        dataGetWay.restart()
    }

    @Test
    fun `RetrieveAllCourses with empty response then do nothing`() {
        //Arrange
        val result = listOf<Course>().toMutableLiveData()
        val retrieveAllCourses = RetrieveAllCourses(result)
        dataGetWay.clearCourses()
        //Act
        retrieveAllCourses()

        //Assert
        Assert.assertTrue(result.value.isNullOrEmpty())
    }

    @Test
    fun `RetrieveAllCourses with response then Update UiLiveData`() {
        //Arrange

        val result = listOf<Course>().toMutableLiveData()

        val retrieveAllCourses = RetrieveAllCourses(result)
        //Act
        retrieveAllCourses()

        //Assert
        Assert.assertTrue(result.value?.isNotEmpty() ?: false)
    }
}