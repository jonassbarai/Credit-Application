package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CreditView
import me.dio.credit.application.system.dto.CreditViewList
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
){
    @PostMapping
    fun saveCredit(@RequestBody creditDTO: CreditDTO): ResponseEntity<String>{
        val credit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Credit ${credit.creditCode} for Customer ${credit.customer?.firstName} saved ")
    }

    @GetMapping
    fun findallByCustomerId(@RequestParam (value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>>{
        val creditViewList: List<CreditViewList> = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)

    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {
       val credit =  this.creditService.findByCreditCode(customerId, creditCode)
       return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }

}