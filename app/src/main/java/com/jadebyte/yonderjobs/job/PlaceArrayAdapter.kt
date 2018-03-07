/*
 * Copyright (c) 2018 Wilberforce Uwadiegwu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jadebyte.yonderjobs.job

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.jadebyte.yonderjobs.R
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Wilberforce on 2/25/18 at 11:43 AM.
 */

class PlaceArrayAdapter(context: Context, resource: Int, private val autocompleteFilter: AutocompleteFilter) : ArrayAdapter<PlaceArrayAdapter.PlaceAutocomplete>(context, resource), Filterable {
    private var googleApiClient: GoogleApiClient? = null
    private var placeAutoCompletes: ArrayList<PlaceAutocomplete>? = null

    fun setGoogleApiClient(googleApiClient: GoogleApiClient?) {
        if (googleApiClient == null || !googleApiClient.isConnected) {
            this.googleApiClient = null
        } else {
            this.googleApiClient = googleApiClient
        }
    }

    override fun getCount(): Int {
        return placeAutoCompletes!!.size
    }

    override fun getItem(position: Int): PlaceAutocomplete? {
        return placeAutoCompletes!![position]
    }

    private fun getPredictions(constraint: CharSequence?): ArrayList<PlaceAutocomplete>? {
        Timber.i("getPredictions: %s", googleApiClient)
        if (googleApiClient != null) {

            val results = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, constraint!!.toString(), null, autocompleteFilter)
            // Wait for predictions, set the timeout.
            val autocompletePredictions = results.await(60, TimeUnit.SECONDS)
            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                Timber.w("getPredictions: %s", status.statusMessage)
                Toast.makeText(context, R.string.error_getting_suggestion, Toast.LENGTH_SHORT).show()
                autocompletePredictions.release()
                return null
            }

            val iterator = autocompletePredictions.iterator()
            val resultList = ArrayList<PlaceAutocomplete>(autocompletePredictions.count)
            while (iterator.hasNext()) {
                val prediction = iterator.next()
                resultList.add(PlaceAutocomplete(prediction.placeId!!, prediction.getFullText(null)))
            }
            // Buffer release
            autocompletePredictions.release()
            Timber.w("getPredictions: %s", resultList.size)
            return resultList
        }
        return null
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                Timber.d("performFiltering")
                val results = Filter.FilterResults()
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    placeAutoCompletes = getPredictions(constraint)
                    if (placeAutoCompletes != null) {
                        // Results
                        results.values = placeAutoCompletes
                        results.count = placeAutoCompletes!!.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
                Timber.d("publishResults")
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    inner class PlaceAutocomplete(var placeId: CharSequence, private var description: CharSequence) {

        override fun toString(): String {
            return description.toString()
        }
    }
}
