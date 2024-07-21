package com.okbatech.okbachat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.okbatech.okbachat.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {

    private lateinit var socket: Socket
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try {
            socket = IO.socket(SOCKET_URL)

            initMain()
        }catch (e: URISyntaxException){
            e.printStackTrace()

        }

    }

    private fun initMain() {
        socket.on(CHAT_KEYS.NEW_MESSAGE, object : Emitter.Listener{
            override fun call(vararg args: Any?) {
                Log.d("OnNew%MessageDebug", "${args[3]}")
            }
        })
        binding.sendHello.setOnClickListener {
            socket.emit(CHAT_KEYS.NEW_MESSAGE, "Hello")
        }
    }

    private object CHAT_KEYS {
        const val NEW_MESSAGE = "new_message"
    }

    companion object {
        private const val SOCKET_URL = "http://10.0.2.2:3000/"
    }

    override fun onDestroy() {
        super.onDestroy()

        if(this::socket.isInitialized){
            socket.disconnect()
            socket.off(CHAT_KEYS.NEW_MESSAGE)
        }
    }
}