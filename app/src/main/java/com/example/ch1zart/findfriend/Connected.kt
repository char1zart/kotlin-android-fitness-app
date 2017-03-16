package com.example.ch1zart.findfriend

import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Connected {

     private var MY_LINK = "http://420ef051.ngrok.io/rest/getmessage/getrad"

 fun getJSON(): JSONArray? {

     val url = URL(String.format(MY_LINK))
     val connection = url.openConnection() as HttpURLConnection


     val reader = BufferedReader(
             InputStreamReader(connection.inputStream))

     val json = StringBuffer(1024)

     var line: String? = null

     while ({ line = reader.readLine(); line }() != null) {
         json.append(line).append("\n")
         println("Считываемые данные: " + line)
     }
     reader.close()


     val obj =JSONArray(json.toString())

         return obj
 }
}

