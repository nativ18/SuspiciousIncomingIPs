import dataModel.IPAddress
import validators.CIDRValidator
import validators.IPv4CIDRRangeValidator
import validators.IPv6CIDRRangeValidator
import validators.NetworkSuspectActivityDetector

/**
 * Allows valid IPv4 CIDR notated and IPv6 prefix notated, both in standard format, to pass.
 * In add more validators and IP formats simply implement [validators.CIDRValidator] interface and adjust the [init] and [isAllowed] function.
 */
class IncomingIPAddressCoordinator {

    // Validators container. Use the to add additional validators in the future.
    private val IPv4Validators = ArrayList<CIDRValidator>()
    private val IPv6Validators = ArrayList<CIDRValidator>()

    // Tries to find networks that suffocating us with many requests.
    private var networkSuspectBehaviorsDetector = NetworkSuspectActivityDetector()

    init {
        // The order of validators in of the lists is important
        IPv4Validators.add(IPv4CIDRRangeValidator())
        IPv6Validators.add(IPv6CIDRRangeValidator())
        networkSuspectBehaviorsDetector = NetworkSuspectActivityDetector()
    }

    // Blocks requests from invalid IP addresses.
    fun isAllowed(incomingIp: IPAddress): Boolean {

        if (incomingIp.type == IPAddressType.IPv4) {
            for (validator in IPv4Validators) {
                val block = !validator.validate(incomingIp)
                if (block) return false
            }
        } else if (incomingIp.type == IPAddressType.IPv6) {
            for (validator in IPv6Validators) {
                val block = !validator.validate(incomingIp)
                if (block) return false
            }
        } else {
            if (enableDebugLogs) {
                println("The IP address is corrupted or of an unfamiliar format:\r\nIP ADDRESS = $incomingIp")
            }
            return false;
        }
        val block = networkSuspectBehaviorsDetector.isIPSuspicious(incomingIp) ?: false
        return !block
    }
}