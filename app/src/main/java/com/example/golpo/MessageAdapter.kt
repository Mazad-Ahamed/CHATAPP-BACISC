package com.example.golpo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_SENT = 1
    private val VIEW_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_SENT) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedHolder(v)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder is SentHolder) holder.bind(msg)
        else if (holder is ReceivedHolder) holder.bind(msg)
    }

    override fun getItemViewType(position: Int): Int {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        return if (messages[position].senderId == uid) VIEW_SENT else VIEW_RECEIVED
    }

    fun addMessage(m: Message) {
        messages.add(m)
        notifyItemInserted(messages.size - 1)
    }

    class SentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.textMessage)
        fun bind(m: Message) {
            text.text = m.text
        }
    }

    class ReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.textMessage)
        fun bind(m: Message) {
            text.text = "${m.senderEmail}: ${m.text}"
        }
    }
}