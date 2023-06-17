package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CreditViewList
import me.dio.credit.application.system.dto.CreditView
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@Controller
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
){
    @PostMapping
    fun saveCredit(@RequestBody creditDTO: CreditDTO): String{
        val credit = this.creditService.save(creditDTO.toEntity())
        return "Credit ${credit.creditCode} for Customer ${credit.customer?.firstName} saved "
    }

    @GetMapping
    fun findallByCustomerId(@RequestParam customerId: Long): List<CreditViewList>{
        return this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())

    }

    @GetMapping
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): CreditView {
       val credit =  this.creditService.findByCreditCode(customerId, creditCode)
        return CreditView(credit)
    }

}