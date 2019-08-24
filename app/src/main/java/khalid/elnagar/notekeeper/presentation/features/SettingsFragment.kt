package khalid.elnagar.notekeeper.presentation.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import khalid.elnagar.notekeeper.R
import khalid.elnagar.notekeeper.presentation.core.bindSummaryToValue
import khalid.elnagar.notekeeper.presentation.core.findPreference

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()

    }
}


private const val ACCOUNT_TAG = "account"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_main, rootKey)
        initAccountSettings()
        findPreference<EditTextPreference>(R.string.storage_key)?.bindSummaryToValue()

    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.settings)
    }


    private fun initAccountSettings() {
        findPreference<Preference>(getString(R.string.account_settings_key))
            ?.setOnPreferenceClickListener {
                activity
                    ?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.settings_container, AccountSettings(), ACCOUNT_TAG)
                    ?.addToBackStack(ACCOUNT_TAG)
                    ?.commit()
                true
            }
    }

}

class AccountSettings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        activity?.title = getString(R.string.account_settings_title)
        setPreferencesFromResource(R.xml.pref_account, rootKey)
        findPreference<EditTextPreference>(R.string.display_name_key)?.bindSummaryToValue()
        findPreference<EditTextPreference>(R.string.email_address_key)?.bindSummaryToValue()
        findPreference<ListPreference>(R.string.favorite_social_key)?.bindSummaryToValue()
    }


}