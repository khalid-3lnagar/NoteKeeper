package khalid.elnagar.notekeeper.presentation.features.note_screen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.Note
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.CoursesLiveData
import khalid.elnagar.notekeeper.domain.RetrieveAllCourses
import khalid.elnagar.notekeeper.domain.toMutableLiveData
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_main.*

const val INTENT_EXTRA_NOTE = "khalid.elnagar.notekeeper.Note"

class NoteActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }
    private val note: Note? by lazy { intent?.getParcelableExtra(INTENT_EXTRA_NOTE) as Note? }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        note?.also { editNote() } ?: addNote()
        setSupportActionBar(toolbar)
        viewModel.courses.observe(this, Observer { initSpinner(it!!) })
        viewModel.retrieveAllCourses()

    }


    private fun addNote() {
        title = getString(R.string.add_note)
    }

    private fun editNote() {
        title = getString(R.string.edit_note)

        txtNoteTitle.text = Editable.Factory().newEditable(note?.noteTitle)
        txt_note_body.text = Editable.Factory().newEditable(note?.note)


    }


    private fun initSpinner(courses: List<Course>) {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            .also { spinner_courses.adapter = it }
            .let { courses.indexOf(note?.course) }
            .also { spinner_courses.setSelection(it) }

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