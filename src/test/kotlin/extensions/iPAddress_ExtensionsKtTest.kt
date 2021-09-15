package extensions

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class String_ExtensionsKtTest

@Test
fun testCIDR() {
    val ip = "192.168.60.55/20"
    val expectedCIDRInBinary = "11111111.11111111.11110000.00000000"
    assertEquals(ip.cidr(), expectedCIDRInBinary)
}