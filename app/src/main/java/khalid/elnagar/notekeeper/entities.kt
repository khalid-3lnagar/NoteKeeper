package khalid.elnagar.notekeeper

import android.os.Parcel
import android.os.Parcelable

//region Note
data class Note(
    val noteTitle: String,
    val note: String,
    val course: Course
) : Parcelable {
    private constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Course::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(noteTitle)
        parcel.writeString(note)
        parcel.writeParcelable(course, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}
//endregion

//region Course

data class Course(
    val courseId: String,
    val title: String,
    var modules: List<Module>
) : Parcelable {

    var modulesCompletionStatus: BooleanArray
        get() = modules.map { it.isComplete }.toBooleanArray()
        set(status) {
            for (i in modules.indices)
                modules[i].isComplete = status[i]
        }

    private constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Module)
    )

    fun getModule(moduleId: String) = modules.filter { it.moduleId == moduleId }[0]

    override fun toString() = title
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(courseId)
        parcel.writeString(title)
        parcel.writeTypedList(modules)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Course> {
        override fun createFromParcel(parcel: Parcel): Course {
            return Course(parcel)
        }

        override fun newArray(size: Int): Array<Course?> {
            return arrayOfNulls(size)
        }
    }
}

//endregion

//region Module
data class Module(val moduleId: String, val title: String, var isComplete: Boolean = false) : Parcelable {
    private constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun toString() = title
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(moduleId)
        parcel.writeString(title)
        parcel.writeByte(if (isComplete) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Module> {
        override fun createFromParcel(parcel: Parcel): Module {
            return Module(parcel)
        }

        override fun newArray(size: Int): Array<Module?> {
            return arrayOfNulls(size)
        }
    }
}
//endregion