package com.example.golpo

data class Message(
    var id: String = "",
    var senderId: String = "",
    var senderEmail: String = "",
    var reciverId : String ="",
    var text: String = "",
    var timestamp: Long = 0L
)
