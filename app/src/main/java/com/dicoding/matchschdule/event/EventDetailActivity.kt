package com.dicoding.matchschdule.event

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dicoding.matchschdule.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.match_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import com.dicoding.matchschdule.R.drawable.ic_added_to_favorites
import com.dicoding.matchschdule.R.drawable.ic_add_to_favorites
import com.dicoding.matchschdule.R.color.colorAccent
import com.dicoding.matchschdule.database.TheSportDbApi
import com.dicoding.matchschdule.database.database
import com.dicoding.matchschdule.favorite.Favorite
import com.dicoding.matchschdule.main.MatchDetail
import com.dicoding.matchschdule.utils.changeFormatDate
import com.dicoding.matchschdule.utils.strToDate

class EventDetailActivity: AppCompatActivity(), EventDetailView {
    private lateinit var event: MatchDetail
    private lateinit var presenter: EventDetailPresenter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_detail)

        event = intent.getParcelableExtra("EVENT")

        val date = strToDate(event.eventDate)
        tv_date.text = changeFormatDate(date)

        home_club.text = event.homeTeam
        home_score.text = event.homeScore

        away_club.text = event.awayTeam
        away_score.text = event.awayScore

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Match Detail"

        favoriteState()

        val apiMatchDetail = TheSportDbApi(event.eventId).getmatchDetail()
        val apiHomeTeam = TheSportDbApi(event.homeTeamId).getTeamDetail()
        val apiAwayTeam = TheSportDbApi(event.awayTeamId).getTeamDetail()
        val gson = Gson()
        presenter = EventDetailPresenter(this, apiMatchDetail, apiHomeTeam, apiAwayTeam, gson)
        presenter.getEventDetail()

        swipe_match.onRefresh {
            presenter.getEventDetail()
        }
        swipe_match.setColorSchemeResources(colorAccent,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)
    }

    override fun showLoading() {
        swipe_match.isRefreshing = true
    }

    override fun hideloading() {
        swipe_match.isRefreshing = false
    }

    override fun showDetail(eventDetail: MatchDetail, homeTeam: MatchDetail, awayTeam: MatchDetail) {
        Picasso.get().load(homeTeam.teamBadge).into(home_img)
        home_club.text = eventDetail.homeTeam
        home_score.text = eventDetail.homeScore
        home_formation.text = eventDetail.homeFormation
        home_goals.text = if(eventDetail.homeGoalDetails.isNullOrEmpty()) "-" else eventDetail.homeGoalDetails?.replace(";", ";\n")
        home_shots.text = eventDetail.homeShots ?: "-"
        home_goalkeeper.text = if(eventDetail.homeLineupGoalKeeper.isNullOrEmpty()) "-" else eventDetail.homeLineupGoalKeeper?.replace("; ", ";\n")
        home_defense.text = if(eventDetail.homeLineupDefense.isNullOrEmpty()) "-" else eventDetail.homeLineupDefense?.replace("; ", ";\n")
        home_midfield.text = if(eventDetail.homeLineupMidfield.isNullOrEmpty()) "-" else eventDetail.homeLineupMidfield?.replace("; ", ";\n")
        home_forward.text = if(eventDetail.homeLineupForward.isNullOrEmpty()) "-" else eventDetail.homeLineupForward?.replace("; ", ";\n")
        home_subtitutes.text = if(eventDetail.homeLineupSubstitutes.isNullOrEmpty()) "-" else eventDetail.homeLineupSubstitutes?.replace("; ", ";\n")

        Picasso.get().load(awayTeam.teamBadge).into(away_img)
        away_club.text = eventDetail.awayTeam
        away_score.text = eventDetail.awayScore
        away_formation.text = eventDetail.awayFormation
        away_goals.text = if(eventDetail.awayGoalsDetails.isNullOrEmpty()) "-" else eventDetail.awayGoalsDetails?.replace(";", ";\n")
        away_shots.text = eventDetail.awayShots ?: "-"
        away_goalkeeper.text = if(eventDetail.awayLineupGoalKeeper.isNullOrEmpty()) "-" else eventDetail.awayLineupGoalKeeper?.replace("; ", ";\n")
        away_defense.text = if(eventDetail.awayLineupDefense.isNullOrEmpty()) "-" else eventDetail.awayLineupDefense?.replace("; ", ";\n")
        away_midfield.text = if(eventDetail.awayLineupMidfield.isNullOrEmpty()) "-" else eventDetail.awayLineupMidfield?.replace("; ", ";\n")
        away_forward.text = if(eventDetail.awayLineupForward.isNullOrEmpty()) "-" else eventDetail.awayLineupForward?.replace("; ", ";\n")
        away_subtitutes.text = if(eventDetail.awayLineupSubstitutes.isNullOrEmpty()) "-" else eventDetail.awayLineupSubstitutes?.replace("; ", ";\n")

        hideloading()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if(isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                    Favorite.EVENT_ID to event.eventId,
                    Favorite.EVENT_NAME to event.eventName,
                    Favorite.EVENT_DATE to event.eventDate,
                    Favorite.HOME_TEAM_ID to event.homeTeamId,
                    Favorite.HOME_TEAM_NAME to event.homeTeam,
                    Favorite.HOME_TEAM_SCORE to event.homeScore,
                    Favorite.AWAY_TEAM_ID to event.awayTeamId,
                    Favorite.AWAY_TEAM_NAME to event.awayTeam,
                    Favorite.AWAY_TEAM_SCORE to event.awayScore)
            }
            snackbar(swipe_match, "Added to favorite").show()
        }catch (e: SQLiteConstraintException){
            snackbar(swipe_match, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use{
                delete(Favorite.TABLE_FAVORITE, "(EVENT_ID = {id})", "id" to event.eventId.orEmpty())
            }
            snackbar(swipe_match, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipe_match, e.localizedMessage).show()
        }
    }

    private fun setFavorite(){
        val icon = if(isFavorite) ic_added_to_favorites else ic_add_to_favorites

        menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, icon)
    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                .whereArgs("(EVENT_ID = {id})", "id" to event.eventId.orEmpty())
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }
}