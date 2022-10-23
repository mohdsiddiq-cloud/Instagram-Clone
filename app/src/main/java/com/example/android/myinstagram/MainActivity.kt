package com.example.android.myinstagram

import android.content.Intent
import com.example.android.myinstagram.Fragments.HomeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.android.myinstagram.Fragments.NotificationFragment
import com.example.android.myinstagram.Fragments.ProfileFragment
import com.example.android.myinstagram.Fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var selectorFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectorFragment=HomeFragment()
        bottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> selectorFragment = HomeFragment()
                R.id.nav_search -> selectorFragment = SearchFragment()
                R.id.nav_add ->{
                    startActivity(Intent(this,PostActivity::class.java))
                }
                R.id.nav_heart -> selectorFragment = NotificationFragment()
                R.id.nav_profile -> selectorFragment = ProfileFragment()
            }
            if (selectorFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectorFragment).commit()
            }
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()

    }


}