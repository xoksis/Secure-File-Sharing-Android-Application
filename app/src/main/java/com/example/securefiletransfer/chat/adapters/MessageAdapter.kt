package com.example.mychatkotlin

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.securefiletransfer.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 1) {
            // inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            // inflate sent.
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            // do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

            holder.itemView.setOnLongClickListener {
                val clipboard = holder.itemView.getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", holder.sentMessage.getText().toString())
                clipboard.setPrimaryClip(clip)

                // Show a message to the user indicating the text was copied
                Toast.makeText(
                    holder.itemView.getContext(),
                    "Text copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnLongClickListener true
            }

        } else {
            // do the stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder

            holder.receiveMessage.text = currentMessage.message

            holder.itemView.setOnLongClickListener {
                val clipboard = holder.itemView.getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip =
                    ClipData.newPlainText("label", holder.receiveMessage.getText().toString())
                clipboard.setPrimaryClip(clip)

                // Show a message to the user indicating the text was copied
                Toast.makeText(
                    holder.itemView.getContext(),
                    "Text copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnLongClickListener true

            }
        }

    }

    class SentViewHolder(itemView: View) : ViewHolder(itemView) {

        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)

    }

}