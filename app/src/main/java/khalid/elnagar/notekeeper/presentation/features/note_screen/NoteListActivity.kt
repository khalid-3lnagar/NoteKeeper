package khalid.elnagar.notekeeper.presentation.features.note_screen

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import khalid.elnagar.notekeeper.Note
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.NotesLiveData
import khalid.elnagar.notekeeper.domain.RetrieveAllNotes
import khalid.elnagar.notekeeper.domain.toMutableLiveData
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import kotlinx.android.synthetic.main.item_note.view.*

class NoteListActivity : AppCompatActivity() {
    val model by lazy { ViewModelProviders.of(this).get(NotesViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


        }
        initRecyclerView()
        model.retrieveAllNotes()
    }

    private fun initRecyclerView() {
        with(rv_list_notes) {
            layoutManager = LinearLayoutManager(this@NoteListActivity)
            adapter = NotesAdapter(model.notes, this@NoteListActivity)
        }


    }

}
//region ViewModel

class NotesViewModel(
    val notes: NotesLiveData = listOf<Note>().toMutableLiveData(),
    val retrieveAllNotes: RetrieveAllNotes = RetrieveAllNotes(notes)
) : ViewModel()

//endregion

//region Recycler Adapter
class NotesAdapter(private val notes: NotesLiveData, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    init {
        notes.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, possition: Int): NoteViewHolder =
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_note, viewGroup, false)
            .let { NoteViewHolder(it) }


    override fun getItemCount(): Int = notes.value?.size ?: 0

    override fun onBindViewHolder(holder: NoteViewHolder, possition: Int) {

        holder.bind(notes.value!![possition])
    }

    class NoteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {


        fun bind(note: Note) {
            view.txt_note.text = note.toString()
        }
    }
}

//endregion
