package khalid.elnagar.notekeeper.presentation.features.main_screen

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.*
import khalid.elnagar.notekeeper.entities.Course
import khalid.elnagar.notekeeper.entities.Note
import khalid.elnagar.notekeeper.presentation.core.addNavToggle
import khalid.elnagar.notekeeper.presentation.core.close
import khalid.elnagar.notekeeper.presentation.core.get
import khalid.elnagar.notekeeper.presentation.core.openDelayed
import khalid.elnagar.notekeeper.presentation.features.INTENT_EXTRA_NOTE_POSITION
import khalid.elnagar.notekeeper.presentation.features.NoteActivity
import khalid.elnagar.notekeeper.presentation.features.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import kotlinx.android.synthetic.main.nav_header.view.*

//region View
class MainActivity : AppCompatActivity() {
    private val model by lazy { ViewModelProviders.of(this).get(NotesViewModel::class.java) }
    private val notesLayoutManager by lazy { androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity) }

    private val notesAdapter by lazy {
        model.retrieveAllNotes()
        NotesAdapter(model.notes, this@MainActivity) { notePosition ->
            startNoteActivity(notePosition)
        }
    }

    private val coursesLayoutManager by lazy { GridLayoutManager(this@MainActivity, 2) }

    private val coursesAdapter by lazy {
        model.retrieveAllCourses()
        CoursesAdapter(model.courses, this@MainActivity)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        drawer_layout.addNavToggle(this)
        nav_view.setNavigationItemSelectedListener(::onNavItemSelected)

        fab.setOnClickListener { startNoteActivity(NEW_NOTE) }
        displayNotes()

    }

    private fun onNavItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_notes -> displayNotes()
            R.id.nav_courses -> displayCourses()
            R.id.nav_settings -> startSettings()
        }
        drawer_layout.close()

        return true
    }

    private fun startSettings() =
        Intent(this, SettingsActivity::class.java).let(::startActivity)

    private fun displayNotes() {
        with(rv_list_items) {
            layoutManager = notesLayoutManager
            adapter = notesAdapter
        }
        nav_view.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {

        with(rv_list_items) {
            layoutManager = coursesLayoutManager
            adapter = coursesAdapter
        }
        nav_view.menu.findItem(R.id.nav_courses).isChecked = true
    }

    private fun startNoteActivity(position: Int) {
        Intent(this, NoteActivity::class.java)
            .apply { position.also { putExtra(INTENT_EXTRA_NOTE_POSITION, it) } }
            .also(::startActivity)
    }

    override fun onResume() {
        super.onResume()
        model.retrieveAllNotes()
        updateNavHeader()
        drawer_layout.openDelayed(1000)
    }

    private fun updateNavHeader() {
        val pref = getDefaultSharedPreferences(this)
        val header: View = nav_view.getHeaderView(0)
        header.nav_txt_name.text =
            pref[getString(R.string.display_name_key), getString(R.string.display_name_default)]
        header.nav_txt_email.text =
            pref[getString(R.string.email_address_key), getString(R.string.email_address_default)]

    }

    override fun onBackPressed() = with(drawer_layout) {
        if (isDrawerOpen(GravityCompat.START))
            close()
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.action_settings)
            startSettings()

        return super.onOptionsItemSelected(item)

    }

}


//endregion

//region ViewModel

class NotesViewModel(
    val notes: NotesLiveData = listOf<Note>().toMutableLiveData(),
    val courses: CoursesLiveData = listOf<Course>().toMutableLiveData(),
    val retrieveAllNotes: RetrieveAllNotes = RetrieveAllNotes(notes),
    val retrieveAllCourses: RetrieveAllCourses = RetrieveAllCourses(courses)
) : ViewModel()

//endregion
