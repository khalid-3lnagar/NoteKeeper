package khalid.elnagar.notekeeper.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import khalid.elnagar.notekeeper.entities.Course
import khalid.elnagar.notekeeper.entities.Note

typealias CoursesLiveData = MutableLiveData<List<Course>>
typealias NotesLiveData = MutableLiveData<List<Note>>

//region Courses Use Cases
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

class RetrieveCourseById(
    private val coursesRepo: CourseRepository = CourseRepository()
) {
    operator fun invoke(courseId: String): Course = coursesRepo.retrieveCourse(courseId)

}

//endregion

//region Notes Use Cases
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
    private val position: MutableLiveData<Int>,
    private val result: MutableLiveData<Note?>,

    private val notesRepo: NotesRepository = NotesRepository()
) {

    operator fun invoke() {
        notesRepo
            .retrieveNotes()
            .let { it.getOrNull(position.value ?: NEW_NOTE) }
            ?.also { result.postValue(it) }
    }

}

class SaveNoteByPosition(
    private val position: MutableLiveData<Int>,
    private val notesRepo: NotesRepository = NotesRepository()
) {
    operator fun invoke(note: Note) {
        notesRepo
            .saveNote(note, position.value ?: NEW_NOTE)
            .also { position.postValue(it) }


    }

}

class RemoveNoteByPosition(
    private val position: LiveData<Int>,
    private val notesRepo: NotesRepository = NotesRepository()
) {
    operator fun invoke() {
        position
            .value
            ?.let { notesRepo.removeNoteByPosition(it) }
    }

}

class IsNoteHasNext(
    private val position: LiveData<Int>,
    private val notesRepo: NotesRepository = NotesRepository()
) {
    operator fun invoke() =
        when (val currentPosition = position.value ?: NEW_NOTE) {
            NEW_NOTE -> false
            else -> currentPosition < notesRepo.retrieveNotes().size - 1
        }
}

class StoreOriginalStateUseCase(
    private val note: LiveData<Note?>,
    private val originalValue: MutableLiveData<ArrayList<String>>
) {
    operator fun invoke() {
        note.value
            ?.apply {
                arrayListOf(noteTitle, this.note, course.courseId).also { originalValue.postValue(it) }
            }
    }
}
//endregion