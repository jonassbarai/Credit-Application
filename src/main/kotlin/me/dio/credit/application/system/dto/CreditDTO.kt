package me.dio.credit.application.system.dto

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    val creditValue: BigDecimal,
    val dayFirstOfInstalment: LocalDate,
    val numberOfInstalments: Int,
    val customer_id: Long
) {
    fun toEntity() = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstalment,
        numberInstallments = this.numberOfInstalments,
        customer = Customer(id = this.customer_id),

        )

}
