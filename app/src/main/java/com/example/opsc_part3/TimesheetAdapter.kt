import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc_part3.EditTimesheet
import com.example.opsc_part3.R
import com.example.opsc_part3.TimesheetData

//Adapter for the timesheets
class TimesheetAdapter(private var context: Context, private var timesheetList: List<TimesheetData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    //Holder for the recycler view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timesheetNameTextView: TextView = itemView.findViewById(R.id.timesheetNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val totalTimeTextView: TextView = itemView.findViewById(R.id.totalTimeTextView)
    }
    //Holder for rest of recycler view
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerTimesheetName: TextView = itemView.findViewById(R.id.headerTimesheetName)
        val headerDate: TextView = itemView.findViewById(R.id.headerDate)
        val headerTotalTime: TextView = itemView.findViewById(R.id.headerTotalTime)
    }
        //Displaying recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val headerView = LayoutInflater.from(context).inflate(R.layout.item_timesheet_header, parent, false)
            HeaderViewHolder(headerView)
        } else {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_timesheet, parent, false)
            ViewHolder(itemView)
        }
    }
    //Setting layout of recycler view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            val headerViewHolder = holder as HeaderViewHolder

            // Set header text
            headerViewHolder.headerTimesheetName.text = "Name"
            headerViewHolder.headerDate.text = "Modified Date"
            headerViewHolder.headerTotalTime.text = "Total Time"
        } else {
            val currentItem = timesheetList[position - 1] // Subtract 1 to account for header
            val viewHolder = holder as ViewHolder

            viewHolder.timesheetNameTextView.text = currentItem.tsName
            viewHolder.dateTextView.text = currentItem.date
            viewHolder.totalTimeTextView.text = currentItem.totalTime
        }
    }
    //getting the item count for home page
    override fun getItemCount() = timesheetList.size + 1 // Add 1 for header

    //Setting items under headings
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }
    //Update data based on new data
    fun updateData(newTimesheet: List<TimesheetData>)
    {
        timesheetList = newTimesheet
        notifyDataSetChanged()
    }
}
