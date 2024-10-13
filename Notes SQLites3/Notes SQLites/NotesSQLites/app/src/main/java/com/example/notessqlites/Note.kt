data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val image: ByteArray? = null  // Make image nullable
)
