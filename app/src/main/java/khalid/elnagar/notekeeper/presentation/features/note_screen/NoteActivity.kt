package khalid.elnagar.notekeeper.presentation.features.note_screen

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import khalid.elnagar.notekeeper.domain.RetrieveNoteByPosition
import khalid.elnagar.notekeeper.domain.toMutableLiveData
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_main.*

const val INTENT_EXTRA_NOTE_POSITION = "khalid.elnagar.notekeeper.Note"
const val POSITION_NOT_SET = -1

class NoteActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        initViewModel()
    }

    private fun initViewModel() {
        with(viewModel) {

            courses.observe(this@NoteActivity, Observer { initSpinner(it!!) })
            retrieveAllCourses()

            note.observe(this@NoteActivity, Observer { it?.also(::editNote) ?: addNote() })
            intent
                .getIntExtra(INTENT_EXTRA_NOTE_POSITION, POSITION_NOT_SET)
                .takeUnless { it == POSITION_NOT_SET }
                ?.also { retrieveNoteByPosition(it) }

        }
    }


    private fun addNote() {
        title = getString(R.string.add_note)
    }

    private fun editNote(note: Note) {
        title = getString(R.string.edit_note)

        txtNoteTitle.text = Editable.Factory().newEditable(note.noteTitle)
        txt_note_body.text = Editable.Factory().newEditable(note.note)
        viewModel.courses.value
            ?.indexOf(note.course)
            ?.also { spinner_courses.setSelection(it) }

    }


    private fun initSpinner(courses: List<Course>) {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            .also { spinner_courses.adapter = it }
            .let { viewModel.note.value }


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
            R.id.action_send_mail -> {
                sendToMail()
                true

            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendToMail() {
        val subject = txtNoteTitle.text.toString()
        val course = spinner_courses.selectedItem as Course
        val text = "checkout what I learned in the pluralsight course \"${course.title}\"\n ${txt_note_body.text}"


        Intent(Intent.ACTION_SEND)
            .apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, text)
            }
            .also(::startActivity)

    }
//endregion

}

class NoteViewModel(
    val courses: CoursesLiveData = listOf<Course>().toMutableLiveData(),
    val note: MutableLiveData<Note?> = MutableLiveData<Note?>().also { it.postValue(null) },
    val retrieveAllCourses: RetrieveAllCourses = RetrieveAllCourses(courses),
    val retrieveNoteByPosition: RetrieveNoteByPosition = RetrieveNoteByPosition(note)
) : ViewModel()