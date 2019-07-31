package khalid.elnagar.notekeeper.presentation.features

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.*
import khalid.elnagar.notekeeper.entities.Course
import khalid.elnagar.notekeeper.entities.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import kotlinx.android.synthetic.main.item_course.view.*
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.android.synthetic.main.item_note.view.txt_note_course

//region View
class MainActivity : AppCompatActivity() {
    private val model by lazy { ViewModelProviders.of(this).get(NotesViewModel::class.java) }

    private val notesLayoutManager by lazy { LinearLayoutManager(this@MainActivity) }
    private val onNoteClicked = { notePosition: Int -> startNoteActivity(notePosition) }
    private val notesAdapter by lazy {
        model.retrieveAllNotes()
        NotesAdapter(model.notes, this@MainActivity, onNoteClicked)
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

        }
        drawer_layout.close()

        return true
    }

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
    }

    override fun onBackPressed() = with(drawer_layout) {
        if (isDrawerOpen(GravityCompat.START))
            close()
        else
            super.onBackPressed()
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
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
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

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

//endregion

//region Courses Adapter
class CoursesAdapter(
    private val courses: CoursesLiveData, lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {
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
            course_item.setOnClickListener { Snackbar.make(it, "$course", Snackbar.LENGTH_SHORT).show() }
        }
    }

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
//endregion

//endregion