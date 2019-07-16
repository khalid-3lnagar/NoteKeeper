package khalid.elnagar.notekeeper.presentation.features.note_screen

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.Note
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.*
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_main.*

const val INTENT_EXTRA_NOTE_POSITION = "khalid.elnagar.notekeeper.Note"


class NoteActivity : AppCompatActivity() {

    private val model by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        initViewModel()
    }

    private fun initViewModel() {
        with(model) {

            courses.observe(this@NoteActivity, Observer { initSpinner(it!!) })
            retrieveAllCourses()

            note.observe(this@NoteActivity, Observer { it?.also(::editNote) ?: addNote() })
            intent
                .getIntExtra(INTENT_EXTRA_NOTE_POSITION, NEW_NODE)
                .takeUnless { it == NEW_NODE }
                ?.also { retrieveNoteByPosition(it) }
                ?.also { position.postValue(it) }


        }
    }


    private fun addNote() {
        title = getString(R.string.add_note)
    }

    private fun editNote(note: Note) {

        title = getString(R.string.edit_note)

        txtNoteTitle.setText(note.noteTitle)
        txt_note_body.setText(note.note)
        model.courses.value
            ?.indexOf(note.course)
            ?.also { spinner_courses.setSelection(it) }

    }


    private fun initSpinner(courses: List<Course>) {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
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
            R.id.action_send_mail -> {
                sendToMail()
                true
            }
            R.id.action_cancel -> {
                model.isCancelling.postValue(true)
                finish()
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

    override fun onPause() {
        super.onPause()
        model
            .isCancelling.value
            .takeIf { it == true }
            ?.let { model.note.value }
            ?.also { storePreviousNoteValue() }
            .let { model.isCancelling.value }
            .takeUnless { it == true }
            ?.also { saveNote() }

    }

    private fun storePreviousNoteValue() = model.note.value?.let { model.saveNoteByPosition(it) }


    private fun saveNote() {
        Note(
            txtNoteTitle.text.toString(),
            txt_note_body.text.toString(),
            spinner_courses.selectedItem as Course

        ).also { model.saveNoteByPosition(it) }


    }
}

class NoteViewModel(
    val courses: CoursesLiveData = listOf<Course>().toMutableLiveData(),
    val note: MutableLiveData<Note?> = MutableLiveData<Note?>().also { it.postValue(null) },
    val position: MutableLiveData<Int> = NEW_NODE.toMutableLiveData(),
    val isCancelling: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val retrieveAllCourses: RetrieveAllCourses = RetrieveAllCourses(courses),
    val retrieveNoteByPosition: RetrieveNoteByPosition = RetrieveNoteByPosition(note),
    val saveNoteByPosition: SaveNoteByPosition = SaveNoteByPosition(position)
) : ViewModel()