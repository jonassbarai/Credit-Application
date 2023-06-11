package me.dio.credit.application.system.service

import me.dio.credit.application.system.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer

    fun findByid(id: Long): Customer

    fun deleteById(id: Long)

}