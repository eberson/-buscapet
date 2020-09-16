package br.com.etecmatao.buscapet.chat

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.model.Message
import br.com.etecmatao.buscapet.model.User
import kotlinx.android.synthetic.main.chat_item_layout.view.*

class MessageAdapter(context: Context) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private val timeFormatter = DateFormat.getTimeFormat(context)

    private val inflater = LayoutInflater.from(context)

    private var user: User? = null
    private val items: MutableList<Message> = mutableListOf()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            val message = items[position]

            itemView.txtChatMessage.text = message.text
            itemView.txtChatSender.text = message.user.getFullName()
            itemView.txtHour.text = timeFormatter.format(message.date)

            itemView.txtChatSender.visibility = VISIBLE
            itemView.chatContainer.setBackgroundResource(R.color.otherMessage)

            user?.let { u ->
                if (u.id == message.user.id){
                    itemView.txtChatSender.visibility = GONE
                    itemView.chatContainer.setBackgroundResource(R.color.myMessage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = inflater.inflate(R.layout.chat_item_layout, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size

    fun setUser(user: User){
        this.user = user
        notifyDataSetChanged()
    }

    fun setMessages(messages: List<Message>){
        items.clear()
        items.addAll(messages)
        notifyDataSetChanged()
    }
}