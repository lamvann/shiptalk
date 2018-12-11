package shiptalk.com.shiptalk.ui.chatroom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.data.Message

class ChatRoomListAapter(
    private var messages: List<Message>?,
    private val context: Context,
    var callback: OnMessageItemClickListener
) : RecyclerView.Adapter<ChatRoomListAapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return messages?.size ?: 0
    }

    class ViewHolder(rowView: View): RecyclerView.ViewHolder(rowView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomListAapter.ViewHolder, position: Int) {
//        holder.itemView.tvExampleMEssage = messages[position].message
    }

    fun updateMessages(messages: List<Message>?){
        this.messages = messages
        this.notifyDataSetChanged()
    }
}