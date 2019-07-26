package khalid.elnagar.notekeeper.presentation.features

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.NEW_NOTE
import khalid.elnagar.notekeeper.domain.NotesLiveData
import khalid.elnagar.notekeeper.domain.RetrieveAllNotes
import khalid.elnagar.notekeeper.domain.toMutableLiveData
import khalid.elnagar.notekeeper.entities.Note
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import kotlinx.android.synthetic.main.item_note.view.*

//region View
class NoteListActivity : AppCompatActivity() {

    private val model by lazy { ViewModelProviders.of(this).get(NotesViewModel::class.java) }
    private val onItemClicked by lazy {
        object : NotesAdapter.OnItemClicked {
            override fun onClick(position: Int) {
                startNoteActivity(position)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { startNoteActivity(NEW_NOTE) }
        initRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        model.retrieveAllNotes()
    }

    private fun initRecyclerView() {
        with(rv_list_notes) {
            layoutManager = LinearLayoutManager(this@NoteListActivity)
            adapter = NotesAdapter(
                model.notes,
                this@NoteListActivity,
                onItemClicked
            )
        }
    }


    private fun startNoteActivity(position: Int) {
        Intent(this, NoteActivity::class.java)
            .apply { position.also { putExtra(INTENT_EXTRA_NOTE_POSITION, it) } }
            .also(::startActivity)
    }

}

//endregion

//region ViewModel

class NotesViewModel(
    val notes: NotesLiveData = listOf<Note>().toMutableLiveData(),
    val retrieveAllNotes: RetrieveAllNotes = RetrieveAllNotes(notes)
) : ViewModel()

//endregion

//region Recycler Adapter
class NotesAdapter(
    private val notes: NotesLiveData,
    lifecycleOwner: LifecycleOwner,
    private val onItemClicked: OnItemClicked
) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    init {
        notes.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    interface OnItemClicked {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): NoteViewHolder =
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_note, viewGroup, false)
            .let { NoteViewHolder(it, onItemClicked) }


    override fun getItemCount(): Int = notes.value?.size ?: 0

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.bind(notes.value!![position], position)
    }

    class NoteViewHolder(private val view: View, private val onItemClicked: OnItemClicked) :
        RecyclerView.ViewHolder(view) {


        fun bind(note: Note, position: Int) {
            with(view) {
                txt_note.text = note.noteTitle
                txt_note_course.text = note.course.title
                note_item.setOnClickListener { onItemClicked.onClick(position) }
            }
        }
    }
}

//endregion
