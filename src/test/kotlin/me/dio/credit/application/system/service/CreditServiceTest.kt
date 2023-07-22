package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun shouldSaveCredit(){
        //given
        val fakeCredit: Credit = buildCredit()
        val fakeId: Long =1L
        every { creditRepository.save(any()) } returns fakeCredit
        every { customerService.findByid(fakeId) } returns fakeCredit.customer!!
        //when
        val actual:Credit =  creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)

        verify(exactly = 1 ) { customerService.findByid(fakeId) }
        verify(exactly = 1 ) { creditRepository.save(fakeCredit)}

    }
    @Test
    fun `should not save by invalid numbers of installment`(){

        //given
        val fakeCredit: Credit = buildCredit(numberInstallments = 49)
        every { creditRepository.save(any()) } returns fakeCredit
        //when

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.save(fakeCredit) }
            .withMessage("Maximum number of installments exceeded")
        //then
        verify(exactly = 0 ) { creditRepository.save(any())}
    }

    @Test
    fun `should not save by invalid day of first installment`(){

        //given
        val fakeCredit: Credit = buildCredit(dayFirstInstallment = LocalDate.now().plusMonths(4))
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.save(fakeCredit) }
            .withMessage("Day of first installment can't be more than 3 months")

        verify(exactly = 0 ) { creditRepository.save(fakeCredit)}
    }

    @Test
    fun shouldfindAllByCustomer(){
        //given
        val fakeId = 1L
        val expectedListOfCredits: List<Credit> = listOf(buildCredit(), buildCredit(), buildCredit())

        every { creditRepository.findAllByCustomerId(fakeId) } returns expectedListOfCredits
        //when
        val actual:List<Credit> =  creditService.findAllByCustomer(fakeId)
        //then
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(expectedListOfCredits)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeId) }
    }

    @Test
    fun shouldfindByCreditCode(){
        //given
        val fakeId = 1L
        val fakeCreditCode :UUID = UUID.randomUUID()
        val fakeCredit = buildCredit(customer = Customer(id = fakeId))
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        val actual = creditService.findByCreditCode(fakeId,fakeCreditCode)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }

    }

    @Test
    fun shouldnotFindByInvalidCreditCode(){
        //given
        val fakeId = 1L
        val fakeCreditCode :UUID = UUID.randomUUID()
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns null
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId,fakeCreditCode) }
            .withMessage("Creditcode $fakeCreditCode not found")

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should not valid customer id`(){
        //given
        val fakeId = 1L
        val fakeCustomerid=2L
        val fakeCreditCode :UUID = UUID.randomUUID()
        val fakeCredit = buildCredit(customer = Customer(id = fakeCustomerid))
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId,fakeCreditCode) }
            .withMessage("Contact admin")

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    //companion objeto é como um metodo/classe estática, sem a necessidade de instanciar
    companion object {
        fun buildCredit(
            creditvalue:BigDecimal =BigDecimal.valueOf(1000.36),
            dayFirstInstallment:LocalDate = LocalDate.now().plusMonths(1L),
            numberInstallments:Int = 5,
            customer: Customer = CustomerServiceTest.buildCustomer()
        ):Credit = Credit(
            creditValue = creditvalue,
            dayFirstInstallment = dayFirstInstallment,
            numberInstallments = numberInstallments,
            customer = customer
        )
    }


}