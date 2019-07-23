package khalid.elnagar.notekeeper.domain

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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
            .let { it.getOrNull(position.value ?: New_Note) }
            ?.also { result.postValue(it) }
    }

}

class SaveNoteByPosition(
    private val position: MutableLiveData<Int>,
    private val notesRepo: NotesRepository = NotesRepository()
) {
    operator fun invoke(note: Note) {
        notesRepo
            .saveNote(note, position.value ?: New_Note)
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
//endregion