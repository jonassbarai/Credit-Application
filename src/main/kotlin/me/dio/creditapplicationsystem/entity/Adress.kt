package me.dio.creditapplicationsystem.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class Adress(
    var zipCode: String = "",
    var street: String =""
)
