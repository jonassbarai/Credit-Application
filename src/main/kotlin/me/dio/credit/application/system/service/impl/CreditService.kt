package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.ICreditService
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.Period
import java.util.*
@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {

    override fun save(credit: Credit): Credit {

        if (credit.numberInstallments > 48)
            throw  BusinessException("Maximum number of installments exceeded")

        if (!validDate(credit.dayFirstInstallment))
            throw  BusinessException("Day of first installment can't be more than 3 months")

        credit.apply {
            customer = customerService.findByid(credit.customer?.id!!)
        }

        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> = this.creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long,creditCode: UUID): Credit {
       val credit =  this.creditRepository.findByCreditCode(creditCode)
            ?: throw BusinessException("Creditcode $creditCode not found")
        return if(credit.customer?.id == customerId) credit else throw BusinessException("Contact admin")
    }

    fun validDate(date: LocalDate): Boolean {
        if(date.isBefore(LocalDate.now().plusMonths(3)))
            return true
         else
            return false
    }
}