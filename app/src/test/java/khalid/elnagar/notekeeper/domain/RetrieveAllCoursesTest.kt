package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import khalid.elnagar.notekeeper.Course
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RetrieveAllCoursesTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `RetrieveAllCourses with empty response then do nothing`() {
        //Arrange
        val repositoryMock = mock<CourseRepository> {
            on { retrieveCourses() } doReturn listOf()
        }
        val result = listOf<Course>().toMutableLiveData()
        val retrieveAllCourses = RetrieveAllCourses(result, repositoryMock)
        //Act
        retrieveAllCourses()

        //Assert
        Assert.assertTrue(result.value.isNullOrEmpty())
    }

    @Test
    fun `RetrieveAllCourses with response then Update UiLiveData`() {
        //Arrange
        val courseMock = mock<Course> {}
        val repositoryMock = mock<CourseRepository> {
            on { retrieveCourses() } doReturn listOf(courseMock)
        }
        val result = listOf<Course>().toMutableLiveData()

        val retrieveAllCourses = RetrieveAllCourses(result, repositoryMock)
        //Act
        retrieveAllCourses()

        //Assert
        Assert.assertTrue(result.value?.isNotEmpty() ?: false)
    }
}