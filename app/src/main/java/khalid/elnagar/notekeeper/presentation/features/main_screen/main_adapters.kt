package khalid.elnagar.notekeeper.presentation.features.main_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.domain.CoursesLiveData
import khalid.elnagar.notekeeper.domain.NotesLiveData
import kotlinx.android.synthetic.main.item_course.view.*
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.android.synthetic.main.item_note.view.txt_note_course

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
            course_item.setOnClickListener {
                Snackbar.make(it, "$course", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
//endregion
