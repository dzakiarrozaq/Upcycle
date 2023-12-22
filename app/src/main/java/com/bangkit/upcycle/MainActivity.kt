package com.bangkit.upcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bangkit.upcycle.apii.ApiConfig
import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.camera.CameraFragment
//import com.bangkit.upcycle.camera.CameraFragment
import com.bangkit.upcycle.databinding.ActivityMainBinding
import com.bangkit.upcycle.home.HomeFragment
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.preferences.dataStore
import com.bangkit.upcycle.profil.ProfilFragment
import com.bangkit.upcycle.ui.LearnFragment
import com.bangkit.upcycle.ui.LocationFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService
    private lateinit var userPreferences: UserPreferences // Initialize UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiConfig.getApiService("token")
        userPreferences = UserPreferences.getInstance(dataStore) // Initialize UserPreferences

        fragment(HomeFragment())

        binding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> fragment(HomeFragment())
                R.id.profil -> fragment(ProfilFragment.newInstance(apiService, userPreferences))
                R.id.camera -> fragment(CameraFragment())
                R.id.location -> fragment(LocationFragment())
                R.id.learn -> fragment(LearnFragment())
                else -> {
                }
            }
            true
        }
    }

    private fun fragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transitionFragment = fragmentManager.beginTransaction()
        transitionFragment.replace(R.id.frameLayout, fragment)
        transitionFragment.commit()
    }
}