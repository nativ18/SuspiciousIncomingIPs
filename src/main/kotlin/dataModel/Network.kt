package dataModel

class Network(var incomingRequests: Int = 0, val networkAddress: String) {

    companion object Constants {
        const val maxPermittedRequest = 10
    }

    fun suspicious(): Boolean {
        return this.incomingRequests > maxPermittedRequest
    }
}