package com.dicoding.matchschdule.event

import com.dicoding.matchschdule.main.MatchDetail

interface EventDetailView {
    fun showLoading()
    fun hideloading()
    fun showDetail(eventDetail: MatchDetail, hometeam: MatchDetail, awayTeam: MatchDetail)

}