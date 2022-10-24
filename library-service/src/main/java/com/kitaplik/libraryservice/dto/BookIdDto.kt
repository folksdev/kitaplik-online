package com.kitaplik.libraryservice.dto

data class BookIdDto @JvmOverloads constructor(
    val bookId: String? = "",
    val isbn: String? = ""
) {
    companion object {
        @JvmStatic
        fun convert(id: String, isbn: String): BookIdDto {
            return BookIdDto(id, isbn)

        }
    }

}
