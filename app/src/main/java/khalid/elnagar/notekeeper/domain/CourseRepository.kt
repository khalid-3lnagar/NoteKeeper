package khalid.elnagar.notekeeper.domain

import khalid.elnagar.notekeeper.Note

class CourseRepository(private val database: InMemoryCoursesGetaway = InMemoryCoursesGetaway.instance) {

    fun retrieveCourses() = database.retrieveCourses()
    fun retrieveCourse(courseId: String) = database.getCourse(courseId)
}

class NotesRepository(private val database: InMemoryCoursesGetaway = InMemoryCoursesGetaway.instance) {

    fun retrieveNotes() = database.retrieveNotes()

    fun saveNote(note: Note, position: Int) = database.saveNote(note, position)

    fun removeNoteByPosition(position: Int) = database.removeNoteByPosition(position)


}