package com.example.onerowcalendar.one_row_calendar

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.onerowcalendar.R

class OneRowCalendar(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs) {

    //TODO Переделать логику переключений и свойства, считающие всякое

    interface OnChangeListener {
        fun onWeekChange(newWeekNum: Int)
        fun onDayChange(newDay: Date?)
    }

    private lateinit var listener: OnChangeListener

    private var viewPager2: ViewPager2
    private var recyclerView: RecyclerView
    private var oneRowAdapter: OneRowAdapter

    private var displayCountDaysOfWeek: Int = 6

    private val countBlocks: Int
        get() = oneRowAdapter.itemCount / displayCountDaysOfWeek
    private var currentWeek = 0
    private val currentWeekPosition: Int
        get() = displayCountDaysOfWeek * currentWeek

    private val currentDayPosition: Int
        get() = ((DateHelper.getCurrDayOfWeek() - 1)
                + displayCountDaysOfWeek
                * (DateHelper.getCurrWeek() - 1))

    private val numOfSelectedDayOfWeek: Int
        get() {
            return oneRowAdapter.currentElement % displayCountDaysOfWeek
        }

    fun addViewPagerAdapter(viewPagerDaysOfWeekAdapter: ViewPagerDaysOfWeekAdapter) {
        viewPager2.adapter = viewPagerDaysOfWeekAdapter
        viewPager2.currentItem = DateHelper.getCurrDayOfWeek() - 1
    }

    fun addOnChangeListener(listener: OnChangeListener) {
        this.listener = listener
    }

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.one_row_calendar, this)

        val attributeArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OneRowCalendar, 0, 0
        )
        viewPager2 = root.findViewById(R.id.viewPager2)
        recyclerView = root.findViewById(R.id.horizontalCalendar)
        oneRowAdapter = OneRowAdapter(
            DateHelper.getDates(attributeArray.getInt(R.styleable.OneRowCalendar_countWeeks, 1)),
            attributeArray.getResourceId(R.styleable.OneRowCalendar_layout_selected, R.layout.item_calendar_selected),
            attributeArray.getResourceId(R.styleable.OneRowCalendar_layout_unselected, R.layout.item_calendar_unselected)
        )

        val snapHelper = SnapToBlock(displayCountDaysOfWeek)
        snapHelper.setSnapBlockCallback(object : SnapToBlock.SnapBlockCallback {
            override fun onBlockSnap(snapPosition: Int) {}
            override fun onBlockSnapped(snapPosition: Int) {
                currentWeek = snapPosition / displayCountDaysOfWeek
                oneRowAdapter.selectElement(snapPosition + numOfSelectedDayOfWeek)
            }
        })

        viewPager2.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeCalendarPosition(position)
            }
        })

        oneRowAdapter.addItemSelectedListener {
            changePagerPosition(it!!.dayOfWeek - 1)
        }

        recyclerView.layoutManager = MaxCountLayoutManager(context, ORIENTATION_HORIZONTAL, false, 6)
        recyclerView.adapter = oneRowAdapter

        snapHelper.attachToRecyclerView(recyclerView)
        
        initState()
    }

    private fun initState() {
        currentWeek = DateHelper.getCurrWeek() - 1
        recyclerView.scrollToPosition(currentWeekPosition)
        oneRowAdapter.selectElement(currentDayPosition)
    }

    fun scrollBlock(countWeeks: Int) {
        currentWeek += countWeeks
        recyclerView.smoothScrollToPosition(currentWeekPosition)
    }

    fun changeCalendarPosition(position: Int) {
        oneRowAdapter.selectElementWithOffset(position - numOfSelectedDayOfWeek)
    }

    private fun changePagerPosition(position: Int) {
        if (position != viewPager2.currentItem) {
            viewPager2.currentItem = position
        }
    }

    fun setCountWeek(countWeeks: Int) {
        oneRowAdapter.updateItems(DateHelper.getDates(countWeeks))
    }

    fun saveState() {

    }

    fun restoreState() {

    }
}