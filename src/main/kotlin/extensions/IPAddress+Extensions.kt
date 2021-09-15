package extensions

import dataModel.CIDR
import dataModel.IPAddress
import enableDebugLogs

fun IPAddress.host(): Int? {
    var parts = ipAddress.split("/")
    if (parts.isNotEmpty()) {
        val ip = parts.first()
        parts = ip.split(".")
        if (parts.isNotEmpty()) {
            try {
                return parts.last().toInt()
            } catch (e: Exception) {
                if (enableDebugLogs) {
                    println("HOST is not a number!")
                }
            }
        }
    }
    return null
}

fun IPAddress.subnetMask(): String? {
    val cidr = cidr ?: return null
    if (!cidr.isValid()) { return null }
    val reminder = 32 - cidr.number
    return "1".repeat(cidr.number) + "0".repeat(reminder)
}

fun IPAddress.subnet(): String? {
    if (enableDebugLogs) {
        println("-- subnet --")
    }
    val cidr = cidr ?: return null
    val subnetMask = subnetMask() ?: return null
    val ip = ipAddress.replace("/${cidr.number}", "")
    if (enableDebugLogs) {
        println("IP = $ip")
        println("subnetMask = $subnetMask")
    }
    val ipParts = ip.split(".")
    var ipInBinary = ""
    for (part in ipParts) {
        val partInBinary = Integer.toBinaryString(part.toInt())
        if (partInBinary.length > 8) {
            return null
        }
        val padding = "0".repeat(8 - partInBinary.length)
        ipInBinary += (padding + partInBinary)
    }
    if (ipInBinary.length != subnetMask.length) {
        if (enableDebugLogs) {
            println("ipInBinary and subnetMask length is different. Something went wrong!")
        }
        return null
    }
    var subnet = ""
    for (i in subnetMask.indices) {
        val a = subnetMask[i].digitToInt()
        val b = ipInBinary[i].digitToInt()
        subnet += a and b
    }
    if (enableDebugLogs) {
        println("subnet = $subnet")
    }
    return subnet
}

fun IPAddress.cidr(): CIDR? {
    val parts = ipAddress.split("/")
    if (parts.isNotEmpty()) {
        try {
            return CIDR(parts.last().toInt(), type)
        } catch (e: Exception) {
            if (enableDebugLogs) {
                println("CIDR is not a number!")
            }
        }
    }
    return null
}