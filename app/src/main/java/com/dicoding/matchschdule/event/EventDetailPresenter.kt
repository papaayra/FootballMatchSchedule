package com.dicoding.matchschdule.event

import com.dicoding.matchschdule.api.ApiRepository
import com.dicoding.matchschdule.api.ApiRespons
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventDetailPresenter(private val view: EventDetailActivity,
                           private val apiMatchDetail: String,
                           private val apiHomeTeam: String,
                           private val apiAwayTeam: String,
                           private val gson: Gson){
    fun getEventDetail(){
        view.showLoading()
        doAsync {
            val matchDetail = gson.fromJson(ApiRepository().doRequest(apiMatchDetail), ApiRespons::class.java)
            val homeTeam = gson.fromJson(ApiRepository().doRequest(apiHomeTeam),ApiRespons::class.java)
            val awayTeam = gson.fromJson(ApiRepository().doRequest(apiAwayTeam), ApiRespons::class.java)

            uiThread {
                view.hideloading()
                view.showDetail(matchDetail.events[0], homeTeam.teams[0], awayTeam.teams[0])
            }
        }
    }


}
