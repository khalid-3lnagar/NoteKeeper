package khalid.elnagar.notekeeper.domain

import android.arch.lifecycle.MutableLiveData
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.Note

typealias CoursesLiveData = MutableLiveData<List<Course>>
typealias NotesLiveData = MutableLiveData<List<Note>>

class RetrieveAllCourses(
    private val result: CoursesLiveData,
    private val coursesRepo: CourseRepository = CourseRepository()
) {
    operator fun invoke() {
        coursesRepo
            .retrieveCourses()
            .also { result.postValue(it) }

    }


}

class RetrieveAllNotes(
    private val result: NotesLiveData,
    private val notesRepo: NotesRepository = NotesRepository()
) {
    operator fun invoke() {
        notesRepo.retrieveNotes()
            .also { result.postValue(it) }
    }

}

class RetrieveNoteByPosition(
    private val result: MutableLiveData<Note?>,
    private val notesRepo: NotesRepository = NotesRepository()
) {

    operator fun invoke(position: Int) {
        notesRepo
            .retrieveNotes()
            .let { it.getOrNull(position) }
            ?.also { result.postValue(it) }
    }


}

class SaveNoteByPosition(
    private val position: MutableLiveData<Int>,
    private val notesRepo: NotesRepository = NotesRepository()
) {

    operator fun invoke(note: Note) {
        notesRepo.saveNote(note, position.value ?: NEW_NODE)


    }


}