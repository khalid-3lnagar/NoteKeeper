package khalid.elnagar.notekeeper.entities

private const val COURSE_NOT_FOUND_EXCEPTION_MESSAGE = "there's no course with this id"

class CourseNotFoundException : Exception(COURSE_NOT_FOUND_EXCEPTION_MESSAGE)