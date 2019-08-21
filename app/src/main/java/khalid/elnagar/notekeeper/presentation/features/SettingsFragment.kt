package khalid.elnagar.notekeeper.presentation.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import khalid.elnagar.notekeeper.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setHomeButtonEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsFragment(), "setting")
            .commit()


    }

}


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_main, rootKey)

        initAccountSettings()

        initStoragePreference()

    }

    private fun initStoragePreference() {
        findPreference<EditTextPreference>(getString(R.string.storage_key))
            ?.apply {
                summary = text
                setOnPreferenceChangeListener { preference, newValue ->
                    preference.summary = newValue.toString()
                    true
                }

            }
    }

    private fun initAccountSettings() {
        findPreference<Preference>(getString(R.string.account_settings_key))
            ?.setOnPreferenceClickListener {
                activity
                    ?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.settings_container, AccountSettings(), "account")
                    ?.addToBackStack("account")
                    ?.commit()
                true
            }
    }


}

class AccountSettings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_account, rootKey)

    }


}