package es.architectcoders.domain

sealed interface Error {
    data class Server(val code: Int) : Error {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Server
            if (code != other.code) return false
            return true
        }

        override fun hashCode(): Int {
            return code
        }
    }

    object Connectivity : Error

    data class Unknown(val message: String) : Error {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Unknown
            if (message != other.message) return false
            return true
        }

        override fun hashCode(): Int {
            return message.hashCode()
        }
    }
}