package khalid.elnagar.notekeeper.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import khalid.elnagar.notekeeper.entities.CourseNotFoundException
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RetrieveCourseByIdTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        InMemoryDataGetWay.restart()
    }

    @Test
    fun `retrieveCourseByIdTest with valid id then return course that match`() {
        val courseId = "android_intents"

        val retrieveCourseById = RetrieveCourseById()

        val result = retrieveCourseById(courseId)

        Assert.assertNotNull(result)


    }

    @Test(expected = CourseNotFoundException::class)
    fun `retrieveCourseByIdTest with not valid id then throw exception`() {
        val courseId = "id"

        val retrieveCourseById = RetrieveCourseById()

        val result = retrieveCourseById(courseId)

        Assert.assertNotNull(result)


    }
}