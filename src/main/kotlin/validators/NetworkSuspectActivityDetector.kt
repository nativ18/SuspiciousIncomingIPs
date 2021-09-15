package validators

import dataModel.IPAddress
import dataModel.Network
import enableDebugLogs

/**
 * Validates that the subnet mask bits are in a correct order.
 * Meaning starting with a sequence of 1s followed by a sequence of 0s.
 */
class NetworkSuspectActivityDetector {

    private val subnetToNetwork = HashMap<String, Network>()

    private fun validate(ip: IPAddress): Boolean {
        if (!ip.isValid()) {
            return false
        }
        val subnetMask = ip.subnetMask ?: return false
        val firstZeroIndex = subnetMask.indexOfFirst { it == '0' }
        for ( i in (firstZeroIndex + 1) until subnetMask.count()) {
            if (subnetMask[i] == '1') {
                if (enableDebugLogs) {
                    println("Corrupted Subnet mask! Subnet mask = $subnetMask")
                }
                return false
            }
        }
        return true
    }

    fun isIPSuspicious(ip: IPAddress): Boolean? {
        if (!validate(ip)) {
            return false
        }
        val subnet = ip.subnet ?: return null
        if (subnetToNetwork.containsKey(subnet)) {
            if (enableDebugLogs) {
                println("hit for ip: " + ip.ipAddress + ". subnet = " + ip.subnet)
            }
            val network = subnetToNetwork[subnet]!!
            network.incomingRequests += 1
            return network.suspicious()
        } else {
            if (enableDebugLogs) {
                println("no hit for ip: " + ip.ipAddress + ". subnet = " + ip.subnet)
            }
            val network = Network(1, subnet)
            subnetToNetwork[subnet] = network
        }
        return false
    }
}