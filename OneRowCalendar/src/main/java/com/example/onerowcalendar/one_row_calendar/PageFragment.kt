package com.example.onerowcalendar.one_row_calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class PageFragment: Fragment() {
    companion object {
        const val positionFragmentName = "positionFragment"

        fun newInstance(positionFragment: Int, pageFragmentClazz: KClass<out PageFragment>): PageFragment {
            val args = Bundle()
            args.putInt(positionFragmentName, positionFragment)
            val fragment = pageFragmentClazz.createInstance()
            fragment.arguments = args
            return fragment
        }
    }
}