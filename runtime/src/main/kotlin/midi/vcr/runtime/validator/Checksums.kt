package midi.vcr.runtime.validator

/**
 * Built-in checksum implementations.
 */
object Checksums {
    /** Roland sum7: sum of data bytes modulo 128. */
    fun rolandSum7(data: ByteArray): Byte =
        data.fold(0) { acc, b -> (acc + (b.toInt() and 0x7F)) and 0x7F }.toByte()

    /** Bitwise XOR of data bytes (7-bit values). */
    fun xor(data: ByteArray): Byte =
        data.fold(0) { acc, b -> acc xor (b.toInt() and 0x7F) }.toByte()

    /** Two's complement checksum: negative sum masked to 7 bits. */
    fun twosCompl(data: ByteArray): Byte {
        val sum = data.fold(0) { acc, b -> acc + (b.toInt() and 0x7F) }
        return ((-sum) and 0x7F).toByte()
    }

    /** Yamaha XOR: same as basic XOR for now (configurable placeholder). */
    fun yamahaXor(data: ByteArray): Byte = xor(data)
}