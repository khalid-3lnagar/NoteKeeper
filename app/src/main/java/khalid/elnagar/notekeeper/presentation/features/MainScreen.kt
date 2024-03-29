package khalid.elnagar.notekeeper.presentation.features

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.*
import khalid.elnagar.notekeeper.entities.Course
import khalid.elnagar.notekeeper.entities.Note
import khalid.elnagar.notekeeper.presentation.core.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import kotlinx.android.synthetic.main.item_course.view.*
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.android.synthetic.main.item_note.view.txt_note_course
import kotlinx.android.synthetic.main.nav_header.view.*

const val FRAGMENT_SETTINGS = "fragment_settings"

//region View
class MainActivity : AppCompatActivity() {
    private val model by lazy { ViewModelProviders.of(this).get(NotesViewModel::class.java) }

    private val notesLayoutManager by lazy { androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity) }
    private val onNoteClicked = { notePosition: Int -> startNoteActivity(notePosition) }
    private val notesAdapter by lazy {
        model.retrieveAllNotes()
        NotesAdapter(model.notes, this@MainActivity, onNoteClicked)
    }

    private val coursesLayoutManager by lazy {
        androidx.recyclerview.widget.GridLayoutManager(
            this@MainActivity,
            2
        )
    }

    private val coursesAdapter by lazy {
        model.retrieveAllCourses()
        CoursesAdapter(model.courses, this@MainActivity)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        drawer_layout.addNavToggle()
        nav_view.setNavigationItemSelectedListener(::onNavItemSelected)

        fab.setOnClickListener { startNoteActivity(NEW_NOTE) }
        displayNotes()

    }

    private fun DrawerLayout.addNavToggle() {

        ActionBarDrawerToggle(
            this@MainActivity, this,
            toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer
        )
            .also(::setDrawerListener)
            .apply { syncState() }

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


    private fun DrawerLayout.close() = closeDrawer(GravityCompat.START)

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

//region Recycler Adapters

//region Notes Adapter
class NotesAdapter(
    private val notes: NotesLiveData,
    lifecycleOwner: LifecycleOwner,
    private val onItemClicked: (position: Int) -> Unit
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    init {
        notes.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): NoteViewHolder =
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_note, viewGroup, false)
            .let { NoteViewHolder(it) }


    override fun getItemCount(): Int = notes.value?.size ?: 0

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes.value!![position]
        with(holder.itemView) {
            txt_note.text = note.noteTitle
            txt_note_course.text = note.course.title
            note_item.setOnClickListener { onItemClicked(position) }
        }
    }

    inner class NoteViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}

//endregion

//region Courses Adapter
class CoursesAdapter(
    private val courses: CoursesLiveData, lifecycleOwner: LifecycleOwner
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {
    init {
        courses.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CourseViewHolder =
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_course, viewGroup, false)
            .let { CourseViewHolder(it) }


    override fun getItemCount(): Int = courses.value?.size ?: 0

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses.value!![position]
        with(holder.itemView) {
            txt_note_course.text = course.title
            course_item.setOnClickListener {
                com.google.android.material.snackbar.Snackbar.make(
                    it,
                    "$course",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    inner class CourseViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}
//endregion

//endregion
