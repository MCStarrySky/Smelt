package me.mical.smelt.util

import java.net.NetworkInterface
import java.util.*
import kotlin.experimental.and


/**
 * @author Ting
 * @date 2022/1/20 11:40
 */
object Validate {

    private val macs = listOf(
        "32-53-63-58-53-FF"
    )

    val validate = macs.containsAll(buildMac())

    fun buildMac(): List<String> {
        val result: MutableList<String> = ArrayList()
        return try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            do {
                val networkInterface = interfaces.nextElement()
                if (Objects.nonNull(networkInterface)) {
                    val mac = networkInterface.hardwareAddress
                    val builder = StringBuilder()
                    if (Objects.nonNull(mac)) {
                        for (i in mac.indices) {
                            if (i != 0) {
                                builder.append("-")
                            }
                            val temp: Int = mac[i].toInt() and 0xff
                            val hex = Integer.toHexString(temp)
                            if (hex.length == 1) {
                                builder.append("0").append(hex)
                            } else {
                                builder.append(hex)
                            }
                        }
                        val macID = builder.toString().uppercase(Locale.getDefault())
                        if (macID.split("-").toTypedArray().size == 6) {
                            result.add(macID)
                        }
                    }
                }
            } while (interfaces.hasMoreElements())
            result
        } catch (e: Throwable) {
            e.printStackTrace()
            result
        }
    }

}