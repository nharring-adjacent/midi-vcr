package midi.vcr.runtime.validator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StreamingValidatorTest {
    @Test
    fun `reject non-7bit data`() {
        val v = StreamingValidator()
        val invalid = 0x80.toByte()
        val ex = assertThrows(IllegalArgumentException::class.java) {
            v.onByte(invalid)
        }
        assertTrue(ex.message!!.contains("Unexpected status byte"))
    }

    @Test
    fun `ignore real-time status bytes`() {
        val v = StreamingValidator()
        v.onByte(0xF8.toByte())  // timing clock
        v.onByte(0x01.toByte())
        assertArrayEquals(byteArrayOf(0xF8.toByte(), 0x01), v.getFrame())
    }

    @Test
    fun `frame accumulation and reset`() {
        val v = StreamingValidator()
        v.onByte(0xF0.toByte())
        v.onByte(0x01.toByte())
        assertArrayEquals(byteArrayOf(0xF0, 0x01), v.getFrame())
        v.reset()
        assertTrue(v.getFrame().isEmpty())
    }
}