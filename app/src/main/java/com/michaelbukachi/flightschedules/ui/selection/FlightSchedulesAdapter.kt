package com.michaelbukachi.flightschedules.ui.selection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.michaelbukachi.flightschedules.R
import com.michaelbukachi.flightschedules.data.api.FlightSchedule
import kotlinx.android.synthetic.main.schedule_list_item.view.*

class FlightSchedulesAdapter(var schedules: List<FlightSchedule>, val listener: OnClickListener) :
    RecyclerView.Adapter<FlightSchedulesAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_list_item, parent, false)
        return ScheduleViewHolder(view)
    }

    fun updateData(newSchedules: List<FlightSchedule>) {
        schedules = newSchedules
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = schedules.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedules[position]
        val firstPoint = schedule.points[0]
        val lastPoint = schedule.points.last()
        holder.destination?.text = "${firstPoint.departureAirport} (${firstPoint.departureTime})"
        holder.arrival?.text = "${lastPoint.arrivalAirport} (${lastPoint.arrivalTime})"
        holder.airlineId?.text = "${firstPoint.airlineId}"
        holder.flightNo?.text = "${firstPoint.flightNo}"
        if (schedule.isDirect) {
            holder.direct?.visibility = View.VISIBLE
        } else {
            holder.direct?.visibility = View.GONE
        }
        holder.container?.setOnClickListener {
            listener.onClick(schedule)
        }
    }

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var destination: TextView? = null
        var arrival: TextView? = null
        var airlineId: TextView? = null
        var flightNo: TextView? = null
        var direct: TextView? = null
        var container: CardView? = null

        init {
            destination = view.departure
            arrival = view.arrival
            airlineId = view.airlineId
            flightNo = view.flightNo
            direct = view.direct
            container = view.container
        }

    }
}