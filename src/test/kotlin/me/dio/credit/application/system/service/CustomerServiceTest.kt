package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import me.dio.credit.application.system.entity.Adress
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK lateinit var customerRepository:CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun shouldCreateCustomer() {
        //given
        val fakeCustomer: Customer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer
        //when
        val actual:Customer = customerService.save(fakeCustomer)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun shouldFindById(){
        //given
        val fakeId: Long = java.util.Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id =fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        //when
        val actual:Customer =  customerService.findByid(fakeId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun shouldNotFindCustomerbyInvalidId(){
        //given
        val fakeId: Long = java.util.Random().nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findByid(fakeId) }
            .withMessage("Id $fakeId not found")

        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun shouldDeleteById(){
        //given
        val fakeId: Long = java.util.Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id =fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs
        //when
        customerService.delete(fakeId)
        //then
        verify(exactly = 1) { customerRepository.findById(fakeId)}
        verify(exactly = 1) { customerRepository.delete(fakeCustomer)}
    }
 //companion objeto é como um metodo/classe estática, sem a necessidade de instanciar
    companion object {
         fun buildCustomer(
            firstName: String = "Jonas",
            lastName: String = "sobrenome",
            cpf: String = "77885089002",
            email: String = "jonas@gmail.com",
            password: String = "1234dafd",
            zipCode: String = "1378945",
            street: String = "rua das amélias",
            income: BigDecimal = BigDecimal.valueOf(1958.32),
            id: Long = 1L
        ) = Customer(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            password = password,
            adress = Adress(
                zipCode = zipCode,
                street = street
            ),
            income = income,
            id = id
        )
    }
}