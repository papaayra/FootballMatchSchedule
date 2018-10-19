package com.dicoding.matchschdule.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.dicoding.matchschdule.R
import com.dicoding.matchschdule.R.id.*
import com.dicoding.matchschdule.event.EventFragment
import com.dicoding.matchschdule.favorite.FavoriteScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private  var leagueId = "4328" //EPL
    private var fixture = 1
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_button.setOnNavigationItemSelectedListener {
                item -> when(item.itemId){
            navigation_prev -> {
                fixture = 1
                openFragment(EventFragment.newInstance(fixture, leagueId))
                return@setOnNavigationItemSelectedListener true
            }
            navigation_next -> {
                fixture = 2
                openFragment(EventFragment.newInstance(fixture, leagueId))
                return@setOnNavigationItemSelectedListener true
            }
            navigation_favorite -> {
                openFragment(FavoriteScheduleFragment())
                return@setOnNavigationItemSelectedListener true

            }

        }
            false
        }
        nav_button.selectedItemId = navigation_prev
    }

    private fun openFragment(fragment: Fragment){
        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.league, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            menu1 -> {
                leagueId = "4328"
            }
            menu2 -> {
                leagueId ="4331"
            }
            menu3 -> {
                leagueId="4332"
            }
            menu4 -> {
                leagueId="4334"
            }
            menu5 -> {
                leagueId = "4335"
            }
        }
        if (nav_button.selectedItemId == navigation_favorite) nav_button.selectedItemId = navigation_prev
        else openFragment(EventFragment.newInstance(fixture, leagueId))
        return super.onOptionsItemSelected(item)
    }
}
