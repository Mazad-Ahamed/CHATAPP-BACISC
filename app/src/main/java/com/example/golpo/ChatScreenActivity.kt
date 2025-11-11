package com.example.golpo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golpo.databinding.ActivityChatScreenBinding
import com.example.golpo.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatScreenBinding
    private  var auth = FirebaseAuth.getInstance()
    private lateinit var adapter: MessageAdapter

    private val database = FirebaseDatabase.getInstance()
    private val messagesRef = database.getReference("messages")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        adapter = MessageAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // send message
        binding.btnSend.setOnClickListener {
            val text = binding.messageInput.text.toString().trim()
            val user = FirebaseAuth.getInstance().currentUser ?: return@setOnClickListener

            if (text.isEmpty()) return@setOnClickListener

            val key = messagesRef.push().key ?: return@setOnClickListener
            val msg = Message(
                id = key,
                senderId = user.uid,
                senderEmail = user.email ?: "",
                text = text,
                timestamp = System.currentTimeMillis()
            )

            messagesRef.child(key).setValue(msg)
                .addOnSuccessListener {
                    binding.messageInput.setText("")
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // listen for new messages
        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, prev: String?) {
                val message = snapshot.getValue(Message::class.java) ?: return
                adapter.addMessage(message)
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, prev: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, prev: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
