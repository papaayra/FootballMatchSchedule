package com.dicoding.matchschdule.event

import com.dicoding.matchschdule.main.MatchDetail

interface EventView {
    fun showLoading()
    fun hideLoading()
    fun showList(data: List<MatchDetail>)

}