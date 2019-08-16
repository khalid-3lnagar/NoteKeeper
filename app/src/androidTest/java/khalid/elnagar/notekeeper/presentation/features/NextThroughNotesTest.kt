package khalid.elnagar.notekeeper.presentation.features

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.InMemoryDataGetWay
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NextThroughNotesTest {

    @Rule
    @JvmField
    var noteListActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun nextThroughNotes() {
        //arrange

        val notes = InMemoryDataGetWay.retrieveNotes()

        //Act
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))
        onView(withId(R.id.rv_list_items)).perform(actionOnItemAtPosition<NotesAdapter.NoteViewHolder>(0, click()))

        //Assert

        for (index in 0 until notes.size) {
            with(notes[index]) {
                onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(course.title)))
                onView(withId(R.id.txt_note_title)).check(matches(withText(noteTitle)))
                onView(withId(R.id.txt_note_body)).check(matches(withText(note)))

            }
            if (index < notes.size - 1)
                onView(withId(R.id.action_next)).perform(click())
        }
        pressBack()
    }
}