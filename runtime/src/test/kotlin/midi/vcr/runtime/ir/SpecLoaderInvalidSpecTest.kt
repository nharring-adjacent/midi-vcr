package midi.vcr.runtime.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files

class SpecLoaderInvalidSpecTest {
    private fun writeTempSpec(yaml: String) = Files.createTempFile("spec", ".yaml").also {
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
    fun `Bytes must specify exactly one of len or until`() {
        // both len and until
        val yamlBoth = yamlWithSpec(
            """
            |      Seq:
            |        nodes:
            |          - Const: '0xF0'
            |          - Bytes: { name: 'payload', len: 2, until: '0xF7' }
            |          - Const: '0xF7'
            """.trimMargin()
        )
        val p1 = writeTempSpec(yamlBoth)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p1) }

        // neither len nor until
        val yamlNeither = yamlWithSpec(
            """
            |      Seq:
            |        nodes:
            |          - Const: '0xF0'
            |          - Bytes: { name: 'payload' }
            |          - Const: '0xF7'
            """.trimMargin()
        )
        val p2 = writeTempSpec(yamlNeither)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p2) }
    }

    @Test
    fun `Repeat must specify exactly one of count or until`() {
        // both count and until
        val yamlBoth = yamlWithSpec(
            """
            |      Repeat:
            |        node: { U7: 'n' }
            |        count: 3
            |        until: 'done'
            """.trimMargin()
        )
        val p1 = writeTempSpec(yamlBoth)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p1) }

        // neither count nor until
        val yamlNeither = yamlWithSpec(
            """
            |      Repeat:
            |        node: { U7: 'n' }
            """.trimMargin()
        )
        val p2 = writeTempSpec(yamlNeither)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p2) }
    }

    @Test
    fun `Negative len and count are rejected`() {
        val yamlNegLen = yamlWithSpec(
            """
            |      Seq:
            |        nodes:
            |          - Bytes: { name: 'p', len: -1 }
            """.trimMargin()
        )
        val p1 = writeTempSpec(yamlNegLen)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p1) }

        val yamlNegCount = yamlWithSpec(
            """
            |      Repeat:
            |        node: { U7: 'n' }
            |        count: -2
            """.trimMargin()
        )
        val p2 = writeTempSpec(yamlNegCount)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p2) }
    }

    @Test
    fun `Const parses hex and decimal strings`() {
        val yamlHex = yamlWithSpec("      Const: '0x7F'\n")
        val p1 = writeTempSpec(yamlHex)
        val spec1 = YamlSpecLoader.load(p1)
        assertEquals(Const(0x7F.toByte()), spec1.commands["test"]) 

        val yamlDec = yamlWithSpec("      Const: '127'\n")
        val p2 = writeTempSpec(yamlDec)
        val spec2 = YamlSpecLoader.load(p2)
        assertEquals(Const(127.toByte()), spec2.commands["test"]) 
    }

    @Test
    fun `Invalid Const value throws`() {
        val yamlBad = yamlWithSpec("      Const: 'ZZ'\n")
        val p = writeTempSpec(yamlBad)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p) }
    }

    @Test
    fun `Seq nodes missing throws`() {
        val yaml = yamlWithSpec(
            """
            |      Seq: {}
            """.trimMargin()
        )
        val p = writeTempSpec(yaml)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p) }
    }

    @Test
    fun `Unknown node type throws`() {
        val yaml = yamlWithSpec(
            """
            |      Foo: {}
            """.trimMargin()
        )
        val p = writeTempSpec(yaml)
        assertThrows(IllegalStateException::class.java) { YamlSpecLoader.load(p) }
    }
}
