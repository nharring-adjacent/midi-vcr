package midi.vcr.runtime.validator

import java.lang.IllegalArgumentException

/**
 * Streaming MIDI SysEx validator.
 * Enforces 7-bit constraints, recognizes real-time bytes, and frames messages.
 */
interface Validator {
    /** Process a single MIDI byte. Throws on invalid data/status. */
    fun onByte(b: Byte)
    /** Clear the current frame buffer. */
    fun reset()
    /** Get the collected bytes so far. */
    fun getFrame(): ByteArray
}

class StreamingValidator : Validator {
    private val buffer = mutableListOf<Byte>()

    /**
     * Process a single MIDI byte. Throws on invalid data/status.
     */
    fun onByte(b: Byte) {
        // Real-time messages (0xF8–0xFF) may be interleaved: always include but do not frame-break
        if ((b.toInt() and 0x80) != 0 && !isRealTimeStatus(b) && b != SYSEX_START && b != SYSEX_END) {
            throw IllegalArgumentException("Unexpected status byte: 0x%02X".format(b))
        }
        buffer.add(b)
    }

    /** Clear the current frame buffer. */
    fun reset() = buffer.clear()

    /** Get the collected bytes so far. */
    fun getFrame(): ByteArray = buffer.toByteArray()

    private fun isRealTimeStatus(b: Byte): Boolean = (b.toInt() and 0xF8) == REAL_TIME_MASK

    companion object {
        private const val REAL_TIME_MASK = 0xF8
        private const val SYSEX_START = 0xF0.toByte()
        private const val SYSEX_END = 0xF7.toByte()
    }
}