package midi.vcr.runtime.ir

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AstTest {
    @Test
    fun `sequence node holds children`() {
        val seq = Seq(listOf(Const(0xF0.toByte()), U7("device_id")))
        assertEquals(2, seq.nodes.size)
        assertEquals(Const(0xF0.toByte()), seq.nodes[0])
    }

    @Test
    fun `bytes node len or until works`() {
        val bytesLen = Bytes(name = "payload", len = 3)
        assertEquals(3, bytesLen.len)

        val bytesUntil = Bytes(name = "payload", until = 0xF7.toByte())
        assertEquals(0xF7.toByte(), bytesUntil.until)
    }
}