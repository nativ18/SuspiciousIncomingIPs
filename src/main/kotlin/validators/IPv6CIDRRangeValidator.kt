package validators
import dataModel.IPAddress
import enableDebugLogs

/**
 * TODO - currently it's just a plaeceholder, copy-pase of the IPv4CIDRRangeValidator validator.
 * TBD
 */
class IPv6CIDRRangeValidator: CIDRValidator {

    override fun validate(ip: IPAddress): Boolean {
        return isValidIPAddress(ip) && isValidCIDR(ip)
    }

    override fun isValidCIDR(ip: IPAddress): Boolean {
       return ip.cidr?.isValid() ?: false
    }

    override fun isValidIPAddress(ip: IPAddress): Boolean {
        val cidr = ip.cidr ?: return false
        var ip = ip.ipAddress
        ip = ip.replace("/" + cidr.number, "")
        val parts = ip.split(".")
        for (part in parts) {
            try {
                val value = part.toInt()
                if (value < 0 || value > 255) {
                    return false
                }
            } catch (e: Exception) {
                return false
            }
        }
        return true
    }
}