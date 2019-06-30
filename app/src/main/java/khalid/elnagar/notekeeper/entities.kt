package khalid.elnagar.notekeeper

//region Note
data class Note(
    val noteTitle: String,
    val note: String,
    val course: Course
)
//endregion

//region Course

data class Course(
    val courseId: String,
    val title: String,
    var modules: List<Module>
) {

    var modulesCompletionStatus: BooleanArray
        get() = modules.map { it.isComplete }.toBooleanArray()
        set(status) {
            for (i in modules.indices)
                modules[i].isComplete = status[i]
        }

    fun getModule(moduleId: String) = modules.filter { it.moduleId == moduleId }[0]

    override fun toString() = title
}

//endregion

//region Module
data class Module(val moduleId: String, val title: String, var isComplete: Boolean = false) {
    override fun toString() = title
}
//endregion