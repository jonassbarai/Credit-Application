package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Positive
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    @field:NotNull val creditValue: BigDecimal,
    @field:Future val dayFirstOfInstalment: LocalDate,
    @field:Positive val numberOfInstalments: Int,
    @field:NotNull val customer_id: Long
) {
    fun toEntity() = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstalment,
        numberInstallments = this.numberOfInstalments,
        customer = Customer(id = this.customer_id),

        )

}
