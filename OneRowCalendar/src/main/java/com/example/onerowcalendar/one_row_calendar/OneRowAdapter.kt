package com.example.onerowcalendar.one_row_calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onerowcalendar.R
import kotlinx.coroutines.flow.MutableStateFlow

class OneRowAdapter(
    private val items: MutableList<Date>,
    @LayoutRes private val selectedLayout: Int,
    @LayoutRes private val unselectedLayout: Int
) : RecyclerView.Adapter<OneRowAdapter.ViewHolder>() {

    fun interface OnItemSelectedListener {
        fun onSelect(date: Date?)
    }

    private var listener: OnItemSelectedListener? = null

    fun addItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

    private val UNSELECTED = 0
    private val SELECTED = 1

    var currentElement: Int = -1
        private set

    fun updateItems(items: List<Date>) {
        items[currentElement].isSelected = true
        val callback = DiffUtilCallback(this.items, items)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(items)
    }

    fun selectElement(newPosition: Int) {
        if (newPosition == currentElement) return
        if (currentElement != -1) changeSelected(false)
        currentElement = newPosition
        changeSelected(true)

        listener?.onSelect(items[currentElement])
    }

    fun selectElementWithOffset(offset: Int) {
        selectElement(currentElement + offset)
    }

    private fun changeSelected(isSelected: Boolean) {
        items[currentElement].isSelected = isSelected
        notifyItemChanged(currentElement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = if (viewType == UNSELECTED) {
            unselectedLayout
        } else {
            selectedLayout
        }
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], this::selectElement)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isSelected) SELECTED else UNSELECTED
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        private val tvDayOfWeek: TextView
        private val tvDayOfMonth: TextView
        private val tvMonth: TextView

        init {
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek)
            tvDayOfMonth = itemView.findViewById(R.id.tvDayOfMonth)
            tvMonth = itemView.findViewById(R.id.tvMonth)
        }

        fun bind(date: Date, listener: OnItemClickListener) {

            tvDayOfWeek.text = DateHelper.getWeekDayName(date.dayOfWeek, DateHelper.WeekdayDisplayMode.TWO_CHAR)
            tvDayOfMonth.text = date.dayOfMonth.toString()
            tvMonth.text = DateHelper.getMonthName(date.month, DateHelper.MonthDisplayMode.THREE_CHAR)

            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
        }
    }
}