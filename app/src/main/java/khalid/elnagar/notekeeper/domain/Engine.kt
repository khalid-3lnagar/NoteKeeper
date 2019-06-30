package khalid.elnagar.notekeeper.domain

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

fun <T> T.toMutableLiveData(): MutableLiveData<T> = MutableLiveData<T>().also { it.postValue(this) }

fun <T> T.toLiveData(): LiveData<T> = MutableLiveData<T>().also { it.postValue(this) }




