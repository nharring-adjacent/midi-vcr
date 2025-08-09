package midi.vcr.runtime.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files

class SpecLoaderValidStructuresTest {
    private fun writeTempSpec(yaml: String) = Files.createTempFile("spec_valid", ".yaml").also {
        Files.writeString(it, yaml)
    }

    private fun yamlWithSpec(specBody: String) = """
        |manufacturer: TestCo
        |model: TestModel
        |transport: sysex7
        |checksum: xor
        |commands:
        |  test:
        |    spec:
        |$specBody
    """.trimMargin()

    @Test
    fun `Choice with two cases parses`() {
        val yaml = yamlWithSpec(
            """
            |      Choice:
            |        0x01:
            |          Seq:
            |            nodes:
            |              - Const: '0x10'
            |        0x02:
            |          U7: 'value'
            """.trimMargin()
        )
        val p = writeTempSpec(yaml)
        val spec = YamlSpecLoader.load(p)
        val node = spec.commands["test"]
        assertTrue(node is Choice)
        val ch = node as Choice
        assertEquals(2, ch.cases.size)
        assertTrue(ch.cases.containsKey(0x01.toByte()))
        assertTrue(ch.cases.containsKey(0x02.toByte()))
        assertTrue(ch.cases[0x01.toByte()] is Seq)
        assertTrue(ch.cases[0x02.toByte()] is U7)
    }

    @Test
    fun `Repeat with count parses`() {
        val yaml = yamlWithSpec(
            """
            |      Repeat:
            |        node: { U7: 'n' }
            |        count: 3
            """.trimMargin()
        )
        val p = writeTempSpec(yaml)
        val spec = YamlSpecLoader.load(p)
        val node = spec.commands["test"]
        assertTrue(node is Repeat)
        val rep = node as Repeat
        assertEquals(3, rep.count)
        assertNull(rep.until)
        assertTrue(rep.node is U7)
    }

    @Test
    fun `Repeat with until parses`() {
        val yaml = yamlWithSpec(
            """
            |      Repeat:
            |        node: { U7: 'n' }
            |        until: 'done'
            """.trimMargin()
        )
        val p = writeTempSpec(yaml)
        val spec = YamlSpecLoader.load(p)
        val node = spec.commands["test"]
        assertTrue(node is Repeat)
        val rep = node as Repeat
        assertNull(rep.count)
        assertEquals("done", rep.until)
    }
}
