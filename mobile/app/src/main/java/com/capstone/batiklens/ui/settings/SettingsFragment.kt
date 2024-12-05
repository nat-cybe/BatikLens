package com.capstone.batiklens.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.batiklens.databinding.FragmentNotificationsBinding
import com.capstone.batiklens.utils.UserPreferences
import com.capstone.batiklens.utils.dataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var pref: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref =  UserPreferences.getInstance(context?.dataStore!!)

        lifecycleScope.launch {
            context?.dataStore?.data?.map {preferences ->
                preferences[booleanPreferencesKey("theme_setting")] ?: false
            }?.collect {isDarkMode ->
                if(isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.darkModeSwitch.isChecked = true
                }else {
                    AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_NO)
                    binding.darkModeSwitch.isChecked = false
                }
            }
        }

        binding.darkModeSwitch.setOnCheckedChangeListener{_: CompoundButton?,isChecked: Boolean ->
            lifecycleScope.launch {
                Log.d("switch", "$isChecked")
                pref.saveThemeSettings(isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}