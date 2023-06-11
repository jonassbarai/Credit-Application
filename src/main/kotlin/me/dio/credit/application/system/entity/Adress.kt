package me.dio.credit.application.system.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class Adress(
    var zipCode: String = "",
    var street: String =""
)
