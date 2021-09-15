package dataModel

import IPAddressClassifier
import IPAddressType
import enableDebugLogs
import extensions.*
import validators.IPv4CIDRRangeValidator
import validators.IPv6CIDRRangeValidator

class IPAddress(val ipAddress: String) {

    val type = IPAddressClassifier.type(ipAddress)
    val cidr: CIDR? = cidr()
    val subnetMask = subnetMask()
    val subnet = subnet()

    override fun toString(): String {
        return ipAddress
    }

    fun isValid(): Boolean {
        return cidr != null && subnetMask != null && subnet != null && type != IPAddressType.Unknown
    }
}