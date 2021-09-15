package validators

import dataModel.IPAddress

interface CIDRValidator{

    fun validate(ip: IPAddress): Boolean
    fun isValidIPAddress(ip: IPAddress): Boolean
    fun isValidCIDR(ip: IPAddress): Boolean
}
