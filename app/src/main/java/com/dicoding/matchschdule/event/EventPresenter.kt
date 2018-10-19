package com.dicoding.matchschdule.event

import com.dicoding.matchschdule.api.ApiRepository
import com.dicoding.matchschdule.api.ApiRespons
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventPresenter(private val view: EventFragment,
                     private val api: String,
                     private val gson: Gson){

    fun getList(){
        view.showLoading()

        doAsync {
            val data = gson.fromJson(ApiRepository().doRequest(api), ApiRespons::class.java)

            uiThread {
                view.hideLoading()
                view.showList(data.events)
            }
        }
    }

}
