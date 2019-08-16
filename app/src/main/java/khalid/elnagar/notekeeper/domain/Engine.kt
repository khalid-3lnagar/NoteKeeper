package khalid.elnagar.notekeeper.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> T.toMutableLiveData(): MutableLiveData<T> = MutableLiveData<T>().also { it.postValue(this) }

fun <T> T.toLiveData(): LiveData<T> = MutableLiveData<T>().also { it.postValue(this) }

val Any.TAG: String get() = javaClass.simpleName
