package com.dicoding.matchschdule.api

import android.net.Uri
import com.dicoding.matchschdule.BuildConfig
import java.net.URL
import java.security.cert.CertPath

class TheSportDBApi(val id: String?){
    private fun urlBuild(path: String?) = Uri.parse(BuildConfig.BASE_URL).buildUpon()
        .appendPath("api")
        .appendPath("v1")
        .appendPath("json")
        .appendPath(BuildConfig.TSDB_API_KEY)
        .appendPath(path)
        .appendQueryParameter("id", id)
        .build().toString()

    fun getprevsechdule() = urlBuild("eventspastleague.php")
    fun getnextsechdule() = urlBuild("eventsnextleague.php")
    fun getmatchDetail() = urlBuild("lookupevent.php")
    fun getTeamDetail() = urlBuild("lookupteam.php")
}
