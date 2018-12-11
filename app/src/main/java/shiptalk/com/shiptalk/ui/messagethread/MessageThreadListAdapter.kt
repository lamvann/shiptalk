package shiptalk.com.shiptalk.ui.messagethread

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_comment.view.*
import kotlinx.android.synthetic.main.layout_item_message.view.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.ui.chatroom.OnMessageItemClickListener

class MessageThreadListAdapter(
    private var messages: List<Message>?,
    private val context: Context
) : RecyclerView.Adapter<MessageThreadListAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return messages?.size ?: 0
    }

    class ViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageThreadListAdapter.ViewHolder, position: Int) {

        messages?.let { messages ->
            holder.itemView.comment.setText(messages[position].message)
//            holder.itemView.avatar.setImageDrawable()
        }
    }

    fun updateMessages(messages: List<Message>?) {
        this.messages = messages
        this.notifyDataSetChanged()
    }
}