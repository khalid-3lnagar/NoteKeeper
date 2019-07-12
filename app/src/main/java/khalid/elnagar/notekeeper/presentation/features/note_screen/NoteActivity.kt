package khalid.elnagar.notekeeper.presentation.features.note_screen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.CoursesLiveData
import khalid.elnagar.notekeeper.domain.RetrieveAllCourses
import khalid.elnagar.notekeeper.domain.toMutableLiveData
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.Serializable

class NoteActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent
            ?.getSerializableExtra(INTENT_EXTRA_NOTE)
            ?.also { updateTitle(it) }

        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        viewModel.courses.observe(this, Observer { initSpinner(it!!) })
        viewModel.retrieveAllCourses()

    }

    private fun updateTitle(it: Serializable) {
        title = when (it as NoteScenario) {
            NoteScenario.ADD_NOTE -> getString(R.string.add_note)
            NoteScenario.EDIT_NOTE -> getString(R.string.edit_note)

        }
    }

    private fun initSpinner(it: List<Course>) {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            .also { spinner_courses.adapter = it }

    }

    //region Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
//endregion

}

class NoteViewModel(
    val courses: CoursesLiveData = listOf<Course>().toMutableLiveData(),
    val retrieveAllCourses: RetrieveAllCourses = RetrieveAllCourses(courses)
) : ViewModel()