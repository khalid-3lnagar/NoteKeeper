package khalid.elnagar.notekeeper.presentation.features


import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.InMemoryDataGetWay
import khalid.elnagar.notekeeper.entities.Course
import khalid.elnagar.notekeeper.entities.Note
import org.hamcrest.Matchers.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NoteCreationTest {

    @Rule
    @JvmField
    var noteListActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun createNewNote() {
        //Arrange
        val dataGetWay = InMemoryDataGetWay
        val course = dataGetWay.getCourse("java_lang")
        val noteTitle = "hey that is Test Note Title"
        val noteBody = "this is body of our Note Test "
        val note = Note(noteTitle, noteBody, course)

        //Act
        onView(withId(R.id.fab)).perform(click())

        val spinnerCourses = onView(withId(R.id.spinner_courses)).perform(click())

        onData(allOf(instanceOf(Course::class.java), equalTo(course)))
            .perform(click())

        spinnerCourses.check(matches(ViewMatchers.withSpinnerText(course.title)))

        onView(withId(R.id.txt_note_title))
            .perform(typeText(noteTitle))
            .check(matches(withText(noteTitle)))

        onView(withId(R.id.txt_note_body))
            .perform(typeText(noteBody))
            .perform(closeSoftKeyboard())
            .check(matches(withText(noteBody)))

        Espresso.pressBack()

        val result = dataGetWay.retrieveNotes().let { it[it.size - 1] }

        //Assert

        Assert.assertEquals(note, result)

    }
}