package midi.vcr.runtime.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class SpecLoaderTest {
    @Test
    fun `load Roland D-110 spec`() {
        val spec = YamlSpecLoader.load(Paths.get("examples/specs/roland.d110.yaml"))
        assertEquals("Roland", spec.manufacturer)
        assertEquals("D-110", spec.model)
        assertTrue(spec.commands.containsKey("read_patch"))
        val readPatch = spec.commands["read_patch"]
        assertNotNull(readPatch)
        assertTrue(readPatch is Seq)
    }

    @Test
    fun `load EMU Orbit spec`() {
        val spec = YamlSpecLoader.load(Paths.get("examples/specs/emu.orbit.yaml"))
        assertEquals("E-Mu", spec.manufacturer)
        assertEquals("Orbit", spec.model)
        assertTrue(spec.commands.containsKey("preset_data_request"))
        val req = spec.commands["preset_data_request"]
        assertNotNull(req)
        assertTrue(req is Seq)
    }
}