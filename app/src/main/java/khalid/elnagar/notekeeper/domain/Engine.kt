package khalid.elnagar.notekeeper.domain

import androidx.lifecycle.MutableLiveData

fun <T> T.toMutableLiveData() = MutableLiveData<T>().also { it.postValue(this) }

val Any.TAG: String get() = javaClass.simpleName
