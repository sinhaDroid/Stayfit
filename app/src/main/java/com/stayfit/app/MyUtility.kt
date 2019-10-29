package com.stayfit.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

/**
 * Revised by kevin on 3/9/2016.
 */
object MyUtility {

    // Download an image using HTTP Get Request
    fun downloadImageusingHTTPGetRequest(urlString: String): Bitmap? {
        var image: Bitmap? = null
        val line: Bitmap

        try {
            val url = URL(urlString)
            val httpConnection = url.openConnection() as HttpURLConnection
            if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = httpConnection.inputStream
                image = getImagefromStream(stream)
            }
            httpConnection.disconnect()
        } catch (e1: UnknownHostException) {
            Log.d("MyDebugMsg", "UnknownHostexception in sendHttpGetRequest")
            e1.printStackTrace()
        } catch (ex: Exception) {
            Log.d("MyDebugMsg", "Exception in sendHttpGetRequest")
            ex.printStackTrace()
        }

        return image
    }

    // Get an image from the input stream
    private fun getImagefromStream(stream: InputStream?): Bitmap? {
        var bitmap: Bitmap? = null
        if (stream != null) {
            bitmap = BitmapFactory.decodeStream(stream)
            try {
                stream.close()
            } catch (e1: IOException) {
                Log.d("MyDebugMsg", "IOException in getImagefromStream()")
                e1.printStackTrace()
            }

        }
        return bitmap
    }


    // Download JSON data (string) using HTTP Get Request
    fun downloadJSONusingHTTPGetRequest(urlString: String): String? {
        var jsonString: String? = null

        try {
            val url = URL(urlString)
            val httpConnection = url.openConnection() as HttpURLConnection
            if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = httpConnection.inputStream
                jsonString = getStringfromStream(stream)
            }
            httpConnection.disconnect()
        } catch (e1: UnknownHostException) {
            Log.d("MyDebugMsg", "UnknownHostexception in downloadJSONusingHTTPGetRequest")
            e1.printStackTrace()
        } catch (ex: Exception) {
            Log.d("MyDebugMsg", "Exception in downloadJSONusingHTTPGetRequest")
            ex.printStackTrace()
        }

        return jsonString
    }

    // Get a string from an input stream
    private fun getStringfromStream(stream: InputStream?): String? {
        var line: String?
        var jsonString: String? = null
        if (stream != null) {
            val reader = BufferedReader(InputStreamReader(stream))
            val out = StringBuilder()
            try {
                do {
                    line = reader.readLine()
                    if (line == null)
                        break

                    out.append(line)
                } while (true)

                reader.close()
                jsonString = out.toString()
            } catch (ex: IOException) {
                Log.d("MyDebugMsg", "IOException in downloadJSON()")
                ex.printStackTrace()
            }

        }
        return jsonString
    }

    // Load JSON string from a local file (in the asset folder)
    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var json: String? = null
        var line: String?
        try {
            val stream = context.assets.open(fileName)
            if (stream != null) {

                val reader = BufferedReader(InputStreamReader(stream))
                val out = StringBuilder()
                do {
                    line = reader.readLine()
                    if (line == null)
                        break

                    out.append(line)
                } while (true)
                reader.close()
                json = out.toString()
            }
        } catch (ex: IOException) {
            Log.d("MyDebugMsg", "IOException in loadJSONFromAsset()")
            ex.printStackTrace()
        }

        return json
    }


    // Send json data via HTTP POST Request
    fun sendHttPostRequest(urlString: String, json: JSONObject) {
        var httpConnection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            httpConnection = url.openConnection() as HttpURLConnection

            httpConnection.doOutput = true
            httpConnection.setChunkedStreamingMode(0)

            val out = OutputStreamWriter(httpConnection.outputStream)
            out.write(json.toString())
            out.close()

            if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = httpConnection.inputStream
                val reader = BufferedReader(InputStreamReader(stream))
                var line: String?
                do {
                    line = reader.readLine()
                    if (line == null)
                        break

                    Log.d("MyDebugMsg:PostRequest", line)  // for debugging purpose
                } while (true)
                reader.close()
                Log.d("MyDebugMsg:PostRequest", "POST request returns ok")
            } else
                Log.d("MyDebugMsg:PostRequest", "POST request returns error")
        } catch (ex: Exception) {
            Log.d("MyDebugMsg", "Exception in sendHttpPostRequest")
            ex.printStackTrace()
        }

        httpConnection?.disconnect()
    }


}
