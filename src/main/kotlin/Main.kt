import dataModel.IPAddress

/**
 *
 * Some enhancements for that project to make it more mature and designed.
 * 1. Support IPv6 in all its formations.
 * 2. Throws exceptions with well explained error message.
 * Present the cause for not allowing the ip request.
 * 3. Add unit tests
 *
 */

// Global params
const val enableDebugLogs = false
enum class IPAddressType { IPv4, IPv6, Unknown }

/**
 * Currently only supporting IPv4 in CIDR notation.
 */
fun populate(incomingIPAddresses:  ArrayList<String>) {
    // Checking parsers and regex expressions
    incomingIPAddresses.add("") // Should fail
    incomingIPAddresses.add("not an ip address") // Should fail
//    incomingIPAddresses.add("2001:0db8:85a3:0000:0000:8a2e:0370:7334/33") // Valid IPv6
//    incomingIPAddresses.add("2001:0db8:85a3:0000:0000:8a2e:0370:7334/129") // Should fail - IPv6 prefix is out of range
    incomingIPAddresses.add("192.172.0.1/33") // Should fail - IPv4 CIDR is out of range
    incomingIPAddresses.add("192.172.0.1") // should fail - not a valid cidr IPv4
    incomingIPAddresses.add("255.256.0.1/31") // should fail - network is out of range

    // Checking incoming requests from the same network. Only first 10 request should pass (declared in Network.Constants)
    for (i in 0..100) {
        incomingIPAddresses.add("192.172.0.$i/20")
    }
    for (i in 0..100) {
        incomingIPAddresses.add("255.255.255.$i/1")
    }
}

fun main(args: Array<String>) {

    // Returns whether ot not the incoming ip could be trusted or not.
    val ipAddressesCoordinator = IncomingIPAddressCoordinator()

    // IP addresses in CIDR Notation
    val incomingIPAddresses = ArrayList<String>()
    populate(incomingIPAddresses)

    // Convert to our IPAddress data model
    val ipAddresses = incomingIPAddresses.map { ip -> IPAddress(ip) }
    for (ipAddress in ipAddresses) {
        val allowed = ipAddressesCoordinator.isAllowed((ipAddress))
        println("IP address $ipAddress: " + if (allowed) "allowed" else "not allowed")
    }
}