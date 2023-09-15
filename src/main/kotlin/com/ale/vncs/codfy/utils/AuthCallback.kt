package com.ale.vncs.codfy.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.BindException
import java.net.ServerSocket
import java.nio.charset.Charset
import java.util.regex.Pattern
import kotlin.concurrent.thread


object AuthCallback {
    fun startListener(port: Int, codeCb: (code: String) -> Unit) {
        try {

            val socket = ServerSocket(port)
            thread() {
                val client = socket.accept()
                println("Authorization granted")
                val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                val out = PrintWriter(client.getOutputStream())

                val code = getCode(reader)

                out.println("HTTP/1.1 200 OK")
                out.println("Content-type: text/html")
                out.println("\r\n")
                val file = getHtml()
                out.println(file)
                out.flush()
                out.close()
                client.close()
                socket.close()
                codeCb(code)
            }
        } catch (ex: BindException) {
            println("Socket already open")
        }
    }

    private fun getCode(reader: BufferedReader): String {
        val line = reader.readLine()
        val p = Pattern.compile("code=(.*)\\sHTTP.*")
        val m = p.matcher(line)
        m.find()
        return m.group(1)
    }

    private fun getHtml(): String? {
        return javaClass.classLoader
            .getResource("html/AuthCallback.html")
            ?.readText(Charset.forName("utf-8"))
            ?.replace("{{appName}}", Constants.APP_NAME)
    }
}
