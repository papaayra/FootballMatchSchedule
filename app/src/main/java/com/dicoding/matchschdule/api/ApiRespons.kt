package com.dicoding.matchschdule.api

import com.dicoding.matchschdule.main.MatchDetail

data class  ApiRespons(
    val events: List<MatchDetail>,
    val teams: List<MatchDetail>
)