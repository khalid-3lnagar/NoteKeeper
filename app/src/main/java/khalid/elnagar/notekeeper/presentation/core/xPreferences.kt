package khalid.elnagar.notekeeper.presentation.core

import android.content.SharedPreferences
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


fun <T : Preference> PreferenceFragmentCompat.findPreference(stringRecourse: Int) =
    findPreference<T>(getString(stringRecourse))

fun Preference.bindSummaryToValue() {

    summary = when (this) {
        is EditTextPreference -> text
        is ListPreference -> entry
        else -> ""
    }
    setOnPreferenceChangeListener { preference, newValue ->
        preference.summary = getSummaryFromNewValue(preference, newValue)
        true
    }
}


private fun getSummaryFromNewValue(preference: Preference?, newValue: Any): CharSequence? =
    when (preference) {
        is EditTextPreference -> newValue.toString()

        is ListPreference -> {
            preference.entryValues
                .indexOf(newValue.toString())
                .let { preference.entries[it] }
        }
        else -> ""
    }

@Suppress("IMPLICIT_CAST_TO_ANY")
inline operator fun <reified T> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String)
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f)
        Int::class -> getInt(key, defaultValue as? Int ?: -1)
        Long::class -> getLong(key, defaultValue as? Long ?: -1L)
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false)
        else -> throw UnsupportedOperationException("Unsupported Operation")

    } as T
}