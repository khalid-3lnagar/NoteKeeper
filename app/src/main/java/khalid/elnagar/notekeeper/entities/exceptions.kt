package khalid.elnagar.notekeeper.entities

private const val COURSE_NOT_FOUND_EXCEPTION_MESSAGE = "there's no such course with this id"
private const val NOTE_NOT_FOUND_EXCEPTION_MESSAGE = "there's no such note with this position"

class CourseNotFoundException : Exception(COURSE_NOT_FOUND_EXCEPTION_MESSAGE)
class NoteNoteFoundException : Exception(NOTE_NOT_FOUND_EXCEPTION_MESSAGE)