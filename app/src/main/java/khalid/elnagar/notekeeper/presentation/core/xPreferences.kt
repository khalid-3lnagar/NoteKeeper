package khalid.elnagar.notekeeper.presentation.core

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
