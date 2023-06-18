package me.dio.credit.application.system.dto

import me.dio.credit.application.system.entity.Adress
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerDTO(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val password: String,
    val zipCode: String,
    val street: String
) {

    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password =this.password,
        adress = Adress(
            zipCode = this.zipCode,
            street = this.street)
    )

}
