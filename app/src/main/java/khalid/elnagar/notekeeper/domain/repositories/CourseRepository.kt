package khalid.elnagar.notekeeper.domain.repositories

import khalid.elnagar.notekeeper.domain.InMemoryCoursesGetaway

class CourseRepository(private val database: InMemoryCoursesGetaway = InMemoryCoursesGetaway.instance) {

    fun retrieveCourses() = database.retrieveCourses()


}