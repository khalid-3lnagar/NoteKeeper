package khalid.elnagar.notekeeper.presentation.features.note_screen

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
private const val SAVED_INSTANCE_ORIGINAL_NOTE = "khalid.elnagar.notekeeper.FIRST_CREATION"
private const val NOTE_TITLE_INDEX = 0

private const val NOTE_TITLE = 1

private const val COURSE_ID_INDEX = 2

class NoteActivity : AppCompatActivity() {

    private val model by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState
            ?.getStringArrayList(SAVED_INSTANCE_ORIGINAL_NOTE)
            ?.also { model.originalValue.postValue(it) }

        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        initViewModel()
    }

    private fun initViewModel() {
        with(model) {

            courses.observe(this@NoteActivity, Observer { initSpinner(it!!) })
            retrieveAllCourses()

            note.observe(this@NoteActivity, Observer { it?.also(::editNote) ?: addNote() })

            position.observe(this@NoteActivity, Observer { onReceivePosition(it) })

            intent
                .getIntExtra(INTENT_EXTRA_NOTE_POSITION, NEW_NODE)
                .also(position::postValue)
                .takeUnless { it == NEW_NODE }
                ?.also { isNewNode.postValue(false) }

        }
    }

    private fun NoteViewModel.onReceivePosition(it: Int?) {
        if (it == NEW_NODE)
            note.postValue(null)
        else
            retrieveNoteByPosition()
    }


    private fun addNote() {
        title = getString(R.string.add_note)
        saveNote()
    }

    private fun editNote(note: Note) {

        title = getString(R.string.edit_note)

        txtNoteTitle.setText(note.noteTitle)
        txt_note_body.setText(note.note)
        model.courses.value
            ?.indexOf(note.course)
            ?.also { spinner_courses.setSelection(it) }

        if (model.originalValue.value.isNullOrEmpty()) {
            arrayListOf(
                note.noteTitle,
                note.note,
                note.course.courseId

            ).also { model.originalValue.postValue(it) }

        }
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
        Log.d(TAG, "onPause")
        super.onPause()
        if (model.isCancelling.value == true) {
            Log.d(TAG, "onPause: cancelling")

            if (model.isNewNode.value != false) {
                Log.d(TAG, "onPause: cancelling: removing New Note")
                model.removeNoteByPosition()
            } else {
                Log.d(TAG, "onPause: cancelling: restore Original Note")
                restorePreviousNoteValue()
            }
        } else {
            saveNote()
        }


    }


    private fun restorePreviousNoteValue() {

        if (!model.originalValue.value.isNullOrEmpty()) {
            Note(
                model.originalValue.value!![NOTE_TITLE_INDEX],
                model.originalValue.value!![NOTE_TITLE],
                model.getCourse(model.originalValue.value!![COURSE_ID_INDEX])

            ).also { model.saveNoteByPosition(it) }
        } else
            Log.d(TAG, "Error while restore Previous Value")

    }


    private fun saveNote() {
        Log.d(TAG, "save Note at ${model.position.value}")
        Note(
            txtNoteTitle.text.toString(),
            txt_note_body.text.toString(),
            spinner_courses.selectedItem as Course

        ).also { model.saveNoteByPosition(it) }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: saving note original value")
        outState?.putStringArrayList(SAVED_INSTANCE_ORIGINAL_NOTE, model.originalValue.value)
    }
}

class NoteViewModel(
    val courses: CoursesLiveData = listOf<Course>().toMutableLiveData(),
    val note: MutableLiveData<Note?> = MutableLiveData(),
    val originalValue: MutableLiveData<ArrayList<String>> = arrayListOf<String>().toMutableLiveData(),
    val position: MutableLiveData<Int> = NEW_NODE.toMutableLiveData(),
    val isNewNode: MutableLiveData<Boolean> = true.toMutableLiveData(),
    val isCancelling: MutableLiveData<Boolean> = false.toMutableLiveData(),

    val retrieveAllCourses: RetrieveAllCourses = RetrieveAllCourses(courses),
    val retrieveNoteByPosition: RetrieveNoteByPosition = RetrieveNoteByPosition(position, note),
    val retrieveCourseById: RetrieveCourseById = RetrieveCourseById(),
    val saveNoteByPosition: SaveNoteByPosition = SaveNoteByPosition(position),
    val removeNoteByPosition: RemoveNoteByPosition = RemoveNoteByPosition(position)
) : ViewModel() {
    fun getCourse(courseId: String): Course = retrieveCourseById(courseId)
}
