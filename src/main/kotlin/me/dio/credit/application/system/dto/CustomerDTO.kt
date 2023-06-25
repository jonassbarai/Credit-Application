package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import me.dio.credit.application.system.entity.Adress
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class CustomerDTO(
    @field:NotEmpty(message = "First name can't be empty") val firstName: String,
    @field:NotEmpty(message = "Last name can't be empty") val lastName: String,
    @field:NotEmpty(message = "CPF can't be empty")
    @field:CPF(message = "Invalid CPF")
    val cpf: String,
    @field:NotNull(value ="Income can't be null") val income: BigDecimal,
    @field:NotEmpty(message = "Email can't be empty")
    @field:Email(message = "Invalid Email")
    val email: String,
    @field:NotEmpty(message = "Password can't be empty") val password: String,
    @field:NotEmpty(message = "Zip Code me can't be empty") val zipCode: String,
    @field:NotEmpty(message = "Street can't be empty") val street: String
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
