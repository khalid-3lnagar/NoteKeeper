package khalid.elnagar.notekeeper.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.domain.repositories.CourseRepository
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
            on { retrieveCourses() } doReturn listOf<Course>().toLiveData()
        }
        val retrieveAllCourses = RetrieveAllCourses(repositoryMock)
        //Act
        val resultLiveData = retrieveAllCourses()

        //Assert
        Assert.assertTrue(resultLiveData.value?.isEmpty() ?: false)
    }

    @Test
    fun `RetrieveAllCourses with response then Update UiLiveData`() {
        //Arrange
        val courseMock = mock<Course> {}
        val repositoryMock = mock<CourseRepository> {
            on { retrieveCourses() } doReturn listOf(courseMock).toLiveData()
        }
        val retrieveAllCourses = RetrieveAllCourses(repositoryMock)
        //Act
        val resultLiveData = retrieveAllCourses()

        //Assert

        Assert.assertTrue(resultLiveData.value?.isNotEmpty() ?: false)
    }
}