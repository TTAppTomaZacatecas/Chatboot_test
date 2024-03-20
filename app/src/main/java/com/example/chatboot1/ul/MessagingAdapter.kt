package com.example.chatboot1.ul

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatboot1.R
import com.example.chatboot1.data.Message
import com.example.chatboot1.utils.Constants.SEND_ID
import com.example.chatboot1.utils.Constants.RECEIVE_ID


class MessagingAdapter: RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {


    var messageList = mutableListOf<Message>()


    inner  class  MessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_message: TextView
        val tv_bot_message: TextView
        init {
            tv_message = itemView.findViewById(R.id.tv_message)
            tv_bot_message = itemView.findViewById(R.id.tv_bot_message)
            itemView.setOnClickListener{
                messageList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return  MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent,false))

    }

    override fun getItemCount(): Int {
        return  messageList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                holder.tv_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tv_bot_message.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.tv_bot_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tv_message.visibility = View.GONE
            }
        }
    }


    fun insertMessage(message: Message){
        this.messageList.add(message)
        notifyItemInserted(messageList.size)
    }

}
