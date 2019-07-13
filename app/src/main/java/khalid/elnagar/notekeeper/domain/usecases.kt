package khalid.elnagar.notekeeper.domain

import android.arch.lifecycle.MutableLiveData
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.Note

typealias CoursesLiveData = MutableLiveData<List<Course>>
typealias NotesLiveData = MutableLiveData<List<Note>>

//get courses names
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

//retrieve Notes
class RetrieveAllNotes(
    private val result: NotesLiveData,
    private val notesRepo: NotesRepository = NotesRepository()
) {
    operator fun invoke() {
        notesRepo.retrieveNotes()
            .also { result.postValue(it) }
    }


}