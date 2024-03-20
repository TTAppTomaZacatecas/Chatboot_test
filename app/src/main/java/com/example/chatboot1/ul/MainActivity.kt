package com.example.chatboot1.ul

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatboot1.R
import com.example.chatboot1.data.Message
import com.example.chatboot1.utils.Constants.SEND_ID
import com.example.chatboot1.utils.Constants.RECEIVE_ID
import com.example.chatboot1.utils.Constants.OPEN_GOOGLE
import com.example.chatboot1.utils.Constants.OPEN_SEARCH
import com.example.chatboot1.utils.BotResponse
import com.example.chatboot1.utils.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var  adapter: MessagingAdapter
    private lateinit var  btn_send:Button
    private  lateinit var et_message: EditText
    private  lateinit var rv_messages : RecyclerView

    var messagesList = mutableListOf<Message>()

    private val botList = listOf("peter", "frances", "Luigi", "igor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_messages = findViewById(R.id.rv_messages)
        btn_send = findViewById(R.id.btn_send)
        et_message = findViewById(R.id.et_message)




        recyclerview()

        clickEvents()

        val random = (0..3).random()
        customBotMessage("Hello maestra! today your're speaking with ${botList[random]},how may i help")
        //enableEdgeToEdge()
       // setContentView(R.layout.activity_main)
       // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
          //  val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
          //  v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
           // insets

    }
    private  fun clickEvents(){
        //send a message
        btn_send.setOnClickListener{
            sendMessage()
        }
        et_message.setOnClickListener{
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.itemCount-1)
                }
            }
        }
    }
    private fun recyclerview(){
        adapter = MessagingAdapter()
        rv_messages.adapter= adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                rv_messages.scrollToPosition(adapter.itemCount -1)
            }
        }
    }
    private  fun sendMessage(){
        val message = et_message.text.toString()
        val timestamp = Time.timeStamp()


        if (message.isNotEmpty()){
            messagesList.add(Message(message, SEND_ID, timestamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timestamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }


    }
    private  fun botResponse(message: String){
        val timestamp = Time.timeStamp()
         GlobalScope.launch {
             delay(1000)

             withContext(Dispatchers.Main){
                 val response = BotResponse.basicResponses(message)

                 messagesList.add(Message(response, RECEIVE_ID, timestamp))

                 adapter.insertMessage(Message(response, RECEIVE_ID, timestamp))

                 rv_messages.scrollToPosition(adapter.itemCount - 1)

                 when(response){
                     OPEN_GOOGLE-> {
                         val site = Intent(Intent.ACTION_VIEW)
                         site.data = Uri.parse("https://www.google.com/")
                         startActivity(site)
                     }
                     OPEN_SEARCH->{
                         val site = Intent(Intent.ACTION_VIEW)
                         val searchTerm: String? =message.substringAfterLast("search")
                         site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                         startActivity(site)
                     }
                 }

             }
         }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}