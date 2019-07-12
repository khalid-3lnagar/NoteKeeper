package khalid.elnagar.notekeeper.domain

class CourseRepository(private val database: InMemoryCoursesGetaway = InMemoryCoursesGetaway.instance) {

    fun retrieveCourses() = database.retrieveCourses()

}

class NotesRepository(private val database: InMemoryCoursesGetaway = InMemoryCoursesGetaway.instance) {
    fun retrieveNotes() = database.retrieveNotes()

}