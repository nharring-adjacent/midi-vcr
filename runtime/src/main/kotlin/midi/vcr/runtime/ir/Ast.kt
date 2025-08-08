package midi.vcr.runtime.ir

/**
 * Abstract syntax tree nodes for the spec IR.
 */
sealed class Node

/** Sequence of child nodes. */
data class Seq(val nodes: List<Node>) : Node()

/** Literal byte value. */
data class Const(val byte: Byte) : Node()

/** Named 7-bit field. */
data class U7(val name: String) : Node()

/** Named 14-bit field (two successive U7 bytes). */
data class U14(val name: String) : Node()

/** Raw byte array, either fixed length or until sentinel. */
data class Bytes(
    val name: String,
    val len: Int? = null,
    val until: Byte? = null
) : Node()

/** Branch into sub-spec based on a key byte. */
data class Choice(val cases: Map<Byte, Node>) : Node()

/** Repeat a sub-node fixed times or until an expression condition. */
data class Repeat(
    val node: Node,
    val count: Int? = null,
    val until: String? = null
) : Node()