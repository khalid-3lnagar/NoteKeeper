package khalid.elnagar.notekeeper.domain

import khalid.elnagar.notekeeper.domain.repositories.CourseRepository

//get courses names
class RetrieveAllCourses(
    private val coursesRepo: CourseRepository = CourseRepository()
) {
    operator fun invoke() = coursesRepo.retrieveCourses()


}