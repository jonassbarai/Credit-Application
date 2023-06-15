package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.dto.CustomerView
import me.dio.credit.application.system.service.impl.CustomerService
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
    fun saveCustomer(@RequestBody customerDTO: CustomerDTO):String{
        val savedCustomer = this.customerService.save(customerDTO.toEntity());
        return "Customer ${savedCustomer.email} saved"
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CustomerView {
      val customer =  this.customerService.findByid(id)
      return  CustomerView(customer)
    }

   @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long) {
        this.customerService.deleteById(id)
    }

    @PatchMapping
    fun updateMapping(@RequestParam(value = "customerId")id: Long,
                      @RequestBody customerUpdateDTO: CustomerUpdateDTO): CustomerView{
        val customer = this.customerService.findByid(id)
        val updatedCustomer = this.customerService.save(customerUpdateDTO.toEntity(customer))
        return CustomerView(customer)
    }
}