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
import org.threeten.bp.Duration
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class FlightSchedulesAdapter(schedules: List<FlightSchedule>, private val listener: OnClickListener) :
    RecyclerView.Adapter<FlightSchedulesAdapter.ScheduleViewHolder>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val schedulesList = ArrayList<FlightSchedule>()

    init {
        schedulesList.addAll(schedules)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_list_item, parent, false)
        return ScheduleViewHolder(view)
    }

    fun updateData(schedules: List<FlightSchedule>) {
        Timber.i("List size: ${schedules.size}")
        schedulesList.clear()
        schedulesList.addAll(schedules)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = schedulesList.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedulesList[position]
        val firstPoint = schedule.points[0]
        val lastPoint = schedule.points.last()
        holder.flight?.text = "${firstPoint.airlineId} ${firstPoint.flightNo}"
        holder.airports?.text = "${firstPoint.departureAirport}\u00B7${lastPoint.arrivalAirport}"
        val depTime = formatter.parse(firstPoint.departureTime)
        val arrTime = formatter.parse(lastPoint.arrivalTime)
        val time = timeFormatter.format(depTime) + "-" + timeFormatter.format(arrTime)
        holder.time?.text = time
        holder.stops?.text = if (schedule.isDirect) "_" else "${schedule.points.size - 1}"
        val duration = Duration.parse(schedule.duration)
        val minutes = duration.toMinutes() % 60
        val hours = duration.toHours()
        holder.duration?.text = "${hours}h ${minutes}m"
        holder.container?.setOnClickListener {
            listener.onClick(schedule)
        }
    }

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var flight: TextView? = null
        var airports: TextView? = null
        var time: TextView? = null
        var stops: TextView? = null
        var duration: TextView? = null
        var container: CardView? = null

        init {
            flight = view.flight
            airports = view.airports
            time = view.time
            stops = view.stops
            duration = view.duration
            container = view.container
        }

    }
}