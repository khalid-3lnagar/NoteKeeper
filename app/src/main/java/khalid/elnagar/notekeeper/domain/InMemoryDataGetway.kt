package khalid.elnagar.notekeeper.domain

import android.arch.lifecycle.LiveData
import khalid.elnagar.notekeeper.Course
import khalid.elnagar.notekeeper.Module
import khalid.elnagar.notekeeper.Note

typealias CoursesLiveData = LiveData<List<Course>>

@Suppress("UNCHECKED_CAST")
class InMemoryCoursesGetaway private constructor(

) {
    private val courses by lazy { mutableListOf<Course>().toMutableLiveData() }
    private val notes by lazy { mutableListOf<Note>().toMutableLiveData() }

    companion object {
        val instance by lazy { InMemoryCoursesGetaway().apply { initializeCourses() } }
    }

    fun retrieveCourses() = courses as CoursesLiveData


    //region Initialization code

    //region initialize Courses

    private fun initializeCourses() {
        ArrayList<Course>()
            .apply {
                add(initializeCourse1())
                add(initializeCourse2())
                add(initializeCourse3())
                add(initializeCourse4())
            }
            .also { courses.postValue(it) }

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

/*
    fun initializeExampleNotes()  {
        val dm = instance

        var course = dm.getCourse("android_intents")
        course.getModule("android_intents_m01").setComplete(true)
        course.getModule("android_intents_m02").setComplete(true)
        course.getModule("android_intents_m03").setComplete(true)
        mNotes.add(
            NoteInfo(
                course, "Dynamic intent resolution",
                "Wow, intents allow components to be resolved at runtime"
            )
        )
        mNotes.add(
            NoteInfo(
                course, "Delegating intents",
                "PendingIntents are powerful; they delegate much more than just a component invocation"
            )
        )

        course = dm.getCourse("android_async")
        course.getModule("android_async_m01").setComplete(true)
        course.getModule("android_async_m02").setComplete(true)
        mNotes.add(
            NoteInfo(
                course, "Service default threads",
                "Did you know that by default an Android Service will tie up the UI thread?"
            )
        )
        mNotes.add(
            NoteInfo(
                course, "Long running operations",
                "Foreground Services can be tied to a notification icon"
            )
        )

        course = dm.getCourse("java_lang")
        course.getModule("java_lang_m01").setComplete(true)
        course.getModule("java_lang_m02").setComplete(true)
        course.getModule("java_lang_m03").setComplete(true)
        course.getModule("java_lang_m04").setComplete(true)
        course.getModule("java_lang_m05").setComplete(true)
        course.getModule("java_lang_m06").setComplete(true)
        course.getModule("java_lang_m07").setComplete(true)
        mNotes.add(
            NoteInfo(
                course, "Parameters",
                "Leverage variable-length parameter lists"
            )
        )
        mNotes.add(
            NoteInfo(
                course, "Anonymous classes",
                "Anonymous classes simplify implementing one-use types"
            )
        )

        course = dm.getCourse("java_core")
        course.getModule("java_core_m01").setComplete(true)
        course.getModule("java_core_m02").setComplete(true)
        course.getModule("java_core_m03").setComplete(true)
        mNotes.add(
            NoteInfo(
                course, "Compiler options",
                "The -jar option isn't compatible with with the -cp option"
            )
        )
        mNotes.add(
            NoteInfo(
                course, "Serialization",
                "Remember to include SerialVersionUID to assure version compatibility"
            )
        )
    }*/

    //endregion

}
