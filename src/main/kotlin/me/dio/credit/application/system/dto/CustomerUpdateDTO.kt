package me.dio.credit.application.system.dto

import jakarta.validation.constraints.NotEmpty
import me.dio.credit.application.system.entity.Customer
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class CustomerUpdateDTO(
    @field:NotEmpty(message = "First name can't be empty") val firstName: String,
    @field:NotEmpty(message = "Last name can't be empty") val lastName: String,
    @field:NotNull(value ="Income can't be null") val income: BigDecimal,
    @field:NotEmpty(message = "Zip code can't be empty") val zipCode: String,
    @field:NotEmpty(message = "street can't be empty") val street: String
) {
    fun toEntity(customer: Customer): Customer {
       customer.firstName =this.firstName
       customer.lastName = this.lastName
       customer.income = this.income
       customer.adress.zipCode = this.zipCode
       customer.adress.street = this.street
       return customer;
    }

}
