package com.example.golpo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golpo.databinding.ItemMessageReceivedBinding
import com.example.golpo.databinding.ItemMessageSentBinding
import com.google.firebase.auth.FirebaseAuth
class MessageAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_SENT = 1
    private val VIEW_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_SENT) {
            val binding = ItemMessageSentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SentHolder(binding)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedHolder(binding)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        when (holder) {
            is SentHolder -> holder.bind(msg)
            is ReceivedHolder -> holder.bind(msg)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        return if (messages[position].senderId == uid) VIEW_SENT else VIEW_RECEIVED
    }

    fun addMessage(m: Message) {
        messages.add(m)
        notifyItemInserted(messages.size - 1)
    }

    class SentHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(m: Message) {
            binding.tvSenderMessage.text = m.text
            binding.sender.text = "${m.senderEmail}"
        }
    }

    class ReceivedHolder(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(m: Message) {
            binding.tvReceiverMessage.text = " ${m.text}"
           // binding.receiverBubble.text =  " ${m.reciverId}"
        }
    }
}
