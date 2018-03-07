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

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wilberforce on 2/25/18 at 2:14 PM.
 */
data class Job(var compName: String? = null, var type: String? = null, var num: String? = null, var techs: String? = null,
               var experience: String? = null, var salary: String? = null, var workArrangement: String? = null, var lat: Double = 0.0,
               var long: Double = 0.0, var info: String? = null, var city: String? = null, var email: String? = null,
               var timeStamp: Long = System.currentTimeMillis()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(compName)
        parcel.writeString(type)
        parcel.writeString(num)
        parcel.writeString(techs)
        parcel.writeString(experience)
        parcel.writeString(salary)
        parcel.writeString(workArrangement)
        parcel.writeDouble(lat)
        parcel.writeDouble(long)
        parcel.writeString(info)
        parcel.writeString(city)
        parcel.writeString(email)
        parcel.writeLong(timeStamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Job> {
        override fun createFromParcel(parcel: Parcel): Job {
            return Job(parcel)
        }

        override fun newArray(size: Int): Array<Job?> {
            return arrayOfNulls(size)
        }
    }

}