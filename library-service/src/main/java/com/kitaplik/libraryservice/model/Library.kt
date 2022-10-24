package com.kitaplik.libraryservice.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
data class Library @JvmOverloads constructor(
    @Id
    @Column(name = "library_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: String? = "",

    @ElementCollection
    val userBook: List<String> = ArrayList()

)
