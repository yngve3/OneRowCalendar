package com.example.onerowcalendar.one_row_calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.reflect.KClass

class ViewPagerDaysOfWeekAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val pageFragmentClazz: KClass<out PageFragment>)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(position, pageFragmentClazz)
    }

    override fun getItemCount(): Int {
        return weekDaysCount
    }

    companion object {
        const val weekDaysCount = 6
    }

}