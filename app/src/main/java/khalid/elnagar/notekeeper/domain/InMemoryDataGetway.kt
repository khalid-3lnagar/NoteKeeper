package khalid.elnagar.notekeeper.domain

import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.Module
import khalid.elnagar.notekeeper.Note


@Suppress("UNCHECKED_CAST")
const val NEW_NODE = -1

class InMemoryCoursesGetaway private constructor(

) {
    private val courses by lazy { mutableListOf<Course>() }
    private val notes by lazy { mutableListOf<Note>() }

    companion object {
        val instance by lazy {
            InMemoryCoursesGetaway().apply {
                initializeCourses()
                initializeExampleNotes()
            }
        }
    }

    fun retrieveCourses() = courses.toList()

    fun retrieveNotes() = notes.toList()

    private fun getCourse(courseTitle: String): Course {

        return courses.filter { it.courseId == courseTitle }[0]
    }

    fun saveNote(note: Note, position: Int) {
        if (position == NEW_NODE)
            notes.add(note)
        else
            notes[position] = note
    }


//region Initialization code

    //region initialize Courses

    private fun initializeCourses() {
        mutableListOf<Course>()
            .apply {
                add(initializeCourse1())
                add(initializeCourse2())
                add(initializeCourse3())
                add(initializeCourse4())
            }
            .also { courses.addAll(it) }

    }

    private fun initializeCourse1(): Course {
        mutableListOf<Module>()
            .apply {
                add(Module("android_intents_m01", "Android Late Binding and Intents"))
                add(Module("android_intents_m01", "Android Late Binding and Intents"))
                add(Module("android_intents_m02", "Component activation with intents"))
                add(Module("android_intents_m03", "Delegation and Callbacks through PendingIntents"))
                add(Module("android_intents_m04", "IntentFilter data tests"))
                add(Module("android_intents_m05", "Working with Platform Features Through Intents"))

            }
            .also { return Course("android_intents", "Android Programming with Intents", it) }
    }

    private fun initializeCourse2(): Course {
        mutableListOf<Module>()
            .apply {
                add(Module("android_async_m01", "Challenges to a responsive user experience"))
                add(Module("android_async_m02", "Implementing long-running operations as a service"))
                add(Module("android_async_m03", "Service lifecycle management"))
                add(Module("android_async_m04", "Interacting with services"))

            }
            .also { return Course("android_async", "Android Async Programming and Services", it) }

    }

    private fun initializeCourse3(): Course {
        mutableListOf<Module>()
            .apply {
                add(Module("java_lang_m01", "Introduction and Setting up Your Environment"))
                add(Module("java_lang_m03", "Variables, Data Types, and Math Operators"))
                add(Module("java_lang_m04", "Conditional Logic, Looping, and Arrays"))
                add(Module("java_lang_m02", "Creating a Simple App"))
                add(Module("java_lang_m05", "Representing Complex Types with Classes"))
                add(Module("java_lang_m06", "Class Initializers and Constructors"))
                add(Module("java_lang_m07", "A Closer Look at Parameters"))
                add(Module("java_lang_m08", "Class Inheritance"))
                add(Module("java_lang_m09", "More About Data Types"))
                add(Module("java_lang_m10", "Exceptions and Error Handling"))
                add(Module("java_lang_m11", "Working with Packages"))
                add(Module("java_lang_m12", "Creating Abstract Relationships with Interfaces"))
                add(Module("java_lang_m13", "Static Members, Nested Types, and Anonymous Classes"))
            }.also { return Course("java_lang", "Java Fundamentals: The Java Language", it) }

    }

    private fun initializeCourse4(): Course {
        mutableListOf<Module>()
            .apply {
                add(Module("java_core_m01", "Introduction"))
                add(Module("java_core_m02", "Input and Output with Streams and Files"))
                add(Module("java_core_m03", "String Formatting and Regular Expressions"))
                add(Module("java_core_m04", "Working with Collections"))
                add(Module("java_core_m05", "Controlling App Execution and Environment"))
                add(Module("java_core_m06", "Capturing Application Activity with the Java Log System"))
                add(Module("java_core_m07", "Multithreading and Concurrency"))
                add(Module("java_core_m08", "Runtime Type Information and Reflection"))
                add(Module("java_core_m09", "Adding Type Metadata with Annotations"))
                add(Module("java_core_m10", "Persisting Objects with Serialization"))
            }
            .also { return Course("java_core", "Java Fundamentals: The Core Platform", it) }

    }

    //endregion

    //region Initialize Notes
    fun initializeExampleNotes() {

        mutableListOf<Note>()
            .apply {
                addAll(initializeNotes1())
                addAll(initializeNotes2())
                addAll(initializeNotes3())
                addAll(initializeNotes4())
            }
            .also { notes.addAll(it) }

    }

    private fun initializeNotes1(): List<Note> {

        val course = getCourse("android_intents")
            .apply {
                getModule("android_intents_m03").isComplete = true
                getModule("android_intents_m02").isComplete = true
            }

        mutableListOf(

            Note(
                "Dynamic intent resolution",
                "Wow, intents allow components to be resolved at runtime",
                course
            ),
            Note(
                "Delegating intents",
                "PendingIntents are powerful; they delegate much more than just a component invocation",
                course
            )

        ).also { return it }
    }

    private fun initializeNotes2(): List<Note> {

        val course = getCourse("android_async")
            .apply {
                getModule("android_async_m01").isComplete = true
                getModule("android_async_m02").isComplete = true
            }



        mutableListOf(

            Note(
                "Service default threads",
                "Did you know that by default an Android Service will tie up the UI thread?", course
            ),
            Note(
                "Long running operations",
                "Foreground Services can be tied to a notification icon", course
            )
        )
            .also { return it }

    }

    private fun initializeNotes3(): List<Note> {
        val course = getCourse("java_lang")

        with(course) {
            getModule("java_lang_m01").isComplete = true
            getModule("java_lang_m02").isComplete = true
            getModule("java_lang_m03").isComplete = true
            getModule("java_lang_m04").isComplete = true
            getModule("java_lang_m05").isComplete = true
            getModule("java_lang_m06").isComplete = true
            getModule("java_lang_m07").isComplete = true
        }


        mutableListOf(
            Note(
                "Parameters",
                "Leverage variable-length parameter lists", course
            ),
            Note(
                "Anonymous classes",
                "Anonymous classes simplify implementing one-use types", course
            )
        )
            .also { return it }

    }

    private fun initializeNotes4(): List<Note> {
        val course = getCourse("java_core")
            .apply {
                getModule("java_core_m01").isComplete = true
                getModule("java_core_m02").isComplete = true
                getModule("java_core_m03").isComplete = true
            }
        mutableListOf(

            Note(
                "Compiler options",
                "The -jar option isn't compatible with with the -cp option", course
            ),
            Note(
                "Serialization",
                "Remember to include SerialVersionUID to assure version compatibility", course
            )

        ).also { return it }
    }
    //endregion

//endregion

}
