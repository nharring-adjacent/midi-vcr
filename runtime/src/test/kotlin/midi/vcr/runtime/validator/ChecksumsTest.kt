package midi.vcr.runtime.validator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChecksumsTest {
    @Test
    fun `roland sum7 simple`() {
        val data = byteArrayOf(1, 2, 3)
        val cs = Checksums.rolandSum7(data)
        assertEquals(((1 + 2 + 3) and 0x7F).toByte(), cs)
    }

    @Test
    fun `xor checksum`() {
        val data = byteArrayOf(0x55, 0x33)
        val cs = Checksums.xor(data)
        assertEquals((0x55 xor 0x33).toByte(), cs)
    }

    @Test
    fun `two's complement checksum`() {
        val data = byteArrayOf(1, 2, 3)
        val sum = 1 + 2 + 3
        val expected = ((-sum) and 0x7F).toByte()
        assertEquals(expected, Checksums.twosCompl(data))
    }
}