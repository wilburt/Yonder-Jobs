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

package com.jadebyte.yonderjobs.intro

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jadebyte.yonderjobs.common.App
import com.jadebyte.yonderjobs.GlideApp
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.utils.Utils
import kotlinx.android.synthetic.main.item_welcome.view.*

/**
 * Created by Wilberforce on 2/20/18 at 4:03 PM.
 */
class IntroAdapter: PagerAdapter() {
    private val texts = intArrayOf(R.string.intro1, R.string.intro2, R.string.intro3)
    private val images = intArrayOf(0, R.drawable.programmer, R.drawable.company)

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as FrameLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(R.layout.item_welcome, container, false)

        if (position != 0) {
            GlideApp.with(itemView.context)
                    .load(images[position])
                    .centerCrop()
                    .into(itemView.introImg)
        }

        val text = Utils.getHTMLString(container.context.getString(texts[position]))
        itemView.introText.typeface = App.customFont
        itemView.introText.text = text

        container.addView(itemView)
        return itemView
    }
}