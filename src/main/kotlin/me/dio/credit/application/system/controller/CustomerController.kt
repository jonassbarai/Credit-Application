package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.dto.CustomerView
import me.dio.credit.application.system.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping
    fun saveCustomer(@RequestBody customerDTO: CustomerDTO):ResponseEntity<String>{
        val savedCustomer = this.customerService.save(customerDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer ${savedCustomer.email} saved")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
      val customer =  this.customerService.findByid(id)
      return  ResponseEntity.status(HttpStatus.OK).body(CustomerView(customer))
    }

   @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long):  ResponseEntity<String>{
        this.customerService.deleteById(id)
       return ResponseEntity.status(HttpStatus.OK).body("Customer Deleted")
    }

    @PatchMapping
    fun updateMapping(@RequestParam(value = "customerId")id: Long,
                      @RequestBody customerUpdateDTO: CustomerUpdateDTO): ResponseEntity<CustomerView>{
        val customer = this.customerService.findByid(id)
        val updatedCustomer = this.customerService.save(customerUpdateDTO.toEntity(customer))
        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(updatedCustomer))
}
}