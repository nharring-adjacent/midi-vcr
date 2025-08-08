package midi.vcr.runtime.ir

import org.yaml.snakeyaml.Yaml
import java.nio.file.Path

/**
 * Loads a spec (YAML) and produces the IR AST.
 */
import java.lang.IllegalArgumentException

/**
 * Represents a parsed device spec, including metadata and per-command AST.
 */
data class Spec(
    val manufacturer: String,
    val model: String,
    val transport: String,
    val checksum: String,
    val commands: Map<String, Node>
)

interface SpecLoader {
    /**
     * Load a YAML device spec file and convert to IR AST.
     */
    fun load(path: Path): Spec
}

/**
 * YAML-based spec loader converting DSL into IR AST.
 */
object YamlSpecLoader : SpecLoader {
    private val yaml = Yaml()

    override fun load(path: Path): Spec {
        val root = yaml.load<Any>(path.toFile().inputStream()) as? Map<*, *>
            ?: throw IllegalArgumentException("Invalid spec file: not a YAML mapping")
        val manufacturer = root["manufacturer"] as? String ?: error("manufacturer missing")
        val model = root["model"] as? String ?: error("model missing")
        val transport = root["transport"] as? String ?: error("transport missing")
        val checksum = root["checksum"] as? String ?: error("checksum missing")
        val cmds = root["commands"] as? Map<*, *>
            ?: error("commands section missing")
        val commands = cmds.entries.associate { entry ->
            val name = entry.key as? String ?: error("Invalid command name: ${entry.key}")
            val cmdMap = entry.value as? Map<*, *>
                ?: error("Invalid command spec for $name")
            val specNode = cmdMap["spec"] ?: error("spec missing for $name")
            name to parseNode(specNode)
        }
        return Spec(manufacturer, model, transport, checksum, commands)
    }

    private fun parseNode(obj: Any?): Node {
        when (obj) {
            is Map<*, *> -> {
                if (obj.size != 1) error("Invalid node map: $obj")
                val (key, value) = obj.entries.first()
                val type = key as? String ?: error("Invalid node type: $key")
                return when (type) {
                    "Seq" -> {
                        val m = value as? Map<*, *> ?: error("Seq content invalid: $value")
                        val list = m["nodes"] as? List<*> ?: error("Seq.nodes missing")
                        Seq(list.map { parseNode(it!!) })
                    }
                    "Const" -> Const(toByte(value))
                    "U7" -> U7(value as String)
                    "U14" -> U14(value as String)
                    "Bytes" -> {
                        val m = value as? Map<*, *> ?: error("Bytes content invalid: $value")
                        val name = m["name"] as? String ?: error("Bytes.name missing")
                        val len = m["len"]?.let { toInt(it) }
                        val until = m["until"]?.let { toByte(it) }
                        Bytes(name, len, until)
                    }
                    "Choice" -> {
                        val m = value as? Map<*, *> ?: error("Choice content invalid: $value")
                        val cases = m.entries.associate { (k, v) -> toByte(k) to parseNode(v) }
                        Choice(cases)
                    }
                    "Repeat" -> {
                        val m = value as? Map<*, *> ?: error("Repeat content invalid: $value")
                        val node = parseNode(m["node"])
                        val count = m["count"]?.let { toInt(it) }
                        val until = m["until"]?.toString()
                        Repeat(node, count, until)
                    }
                    else -> error("Unknown node type: $type")
                }
            }
            else -> error("Invalid node object: $obj")
        }
    }

    private fun toInt(o: Any) = when (o) {
        is Number -> o.toInt()
        is String -> o.toIntOrNull() ?: error("Invalid int value: $o")
        else -> error("Cannot parse int from $o")
    }

    private fun toByte(o: Any?) = when (o) {
        is Number -> o.toByte()
        is String -> if (o.startsWith("0x") || o.startsWith("0X"))
            o.substring(2).toInt(16).toByte() else o.toInt().toByte()
        else -> error("Cannot parse byte from $o")
    }
}