package me.dio.creditapplicationsystem.entity

data class Customer(
    var firstName: String = "",
    var lastName: String ="",
    val cpf: String ="",
    var email: String ="",
    var password: String ="",
    var adress: Adress = Adress(),
    var credit:List<Credit> = mutableListOf(),
    var id: Long? =null
)
