package dataModel

import IPAddressType

class CIDR(public val number: Int, private val type: IPAddressType) {

    fun isValid(): Boolean {
        if (type == IPAddressType.IPv4) {
            return number in 1..32
        } else if (type == IPAddressType.IPv6) {
            return number in 1..128
        } else {
            return false
        }
    }
}