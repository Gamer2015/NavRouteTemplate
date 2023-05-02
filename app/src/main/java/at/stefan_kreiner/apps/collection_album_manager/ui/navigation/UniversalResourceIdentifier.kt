package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

class UniversalResourceIdentifierPath(vararg val parts: String) {
    init {
        parts.forEach { part ->
            listOf("?").forEach { invalidCharacter ->
                if(part.contains(invalidCharacter)) {
                    throw Exception("Invalid path part character \"$invalidCharacter\" in: \"$part\" ($this)")
                }
            }
        }
    }

    override fun toString(): String {
        return parts.joinToString("/")
    }
}
typealias UniversalResourceIdentifierQuery = String
