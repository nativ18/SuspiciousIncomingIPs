/**
 *  Classify the IP address - here we can add more IP forms.
 */
object IPAddressClassifier {

    object Constants {
        const val IPv6AddressPrefixNotated = "\\A(?:[A-F0-9]{1,4}:){7}[A-F0-9]{1,4}\\Z(\$|/[1-128])?"
        const val IPv4AddressCIDRNotated = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\/[0-3]{1}[0-2]{0,1}"
    }

    private val IPv4regex = Constants.IPv4AddressCIDRNotated.toRegex()
    // IPv6 in standard notation (e.g.: 1762:0:0:0:0:B03:1:AF18/64).
    private val IPv6regex = Constants.IPv6AddressPrefixNotated.toRegex()

    fun type(ip: String): IPAddressType {
        return if (IPv4regex.matches(ip))
            IPAddressType.IPv4
        else if (IPv6regex.matches(ip))
            IPAddressType.IPv6
        else
            IPAddressType.Unknown
    }
}
