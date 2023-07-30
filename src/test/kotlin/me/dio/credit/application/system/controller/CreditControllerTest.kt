package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.entity.Adress
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.CustomerServiceTest
import me.dio.credit.application.system.service.impl.CustomerService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockKExtension::class)
class CreditControllerTest {
    @Autowired
    private lateinit var creditRepository: CreditRepository
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL: String ="/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.save(CustomerServiceTest.buildCustomer(id = 1L))
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should save credit and return status 201 `(){
        //given
        val creditDTO = builderCrediDTO()
        val valueAsString:String = objectMapper.writeValueAsString(creditDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType(MediaType.APPLICATION_JSON))
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `should not save credit by invalid numbers of installmest and return status 400 `(){
        //given
        val creditDTO = builderCrediDTO(numberInstallments = 49)
        val valueAsString:String = objectMapper.writeValueAsString(creditDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType(MediaType.APPLICATION_JSON))
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save credit by invalid customer id and return status 400 `(){
        //given
        val creditDTO = builderCrediDTO(customer_id = 3L)
        val valueAsString:String = objectMapper.writeValueAsString(creditDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType(MediaType.APPLICATION_JSON))
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all credits by customer id and return status 200`(){
        val fakeId = 1L
        creditRepository.save(builderCrediDTO().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$URL?customerId=$fakeId")
                .contentType(MediaType(MediaType.APPLICATION_JSON)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by creditcode and return 200status`(){
        val fakeId = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit:Credit =builderCrediDTO().toEntity()
        fakeCredit.creditCode = fakeCreditCode
        creditRepository.save(fakeCredit)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$URL/$fakeCreditCode?customerId=$fakeId")
                .contentType(MediaType(MediaType.APPLICATION_JSON)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credit by creditcode and return 400 status`(){
        val fakeId = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit:Credit =builderCrediDTO().toEntity()
        creditRepository.save(fakeCredit)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$URL/$fakeCreditCode?customerId=$fakeId")
                .contentType(MediaType(MediaType.APPLICATION_JSON)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by creditcode but invalid customer return 400 status`(){
        val fakeId = 2L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit:Credit =builderCrediDTO().toEntity()
        fakeCredit.creditCode = fakeCreditCode
        creditRepository.save(fakeCredit)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$URL/$fakeCreditCode?customerId=$fakeId")
                .contentType(MediaType(MediaType.APPLICATION_JSON)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }


    private fun builderCrediDTO(
        creditvalue: BigDecimal = BigDecimal.valueOf(1000.36),
        dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(1L),
        numberInstallments:Int = 5,
        customer_id:Long = 1L
    ): CreditDTO = CreditDTO(
        creditValue = creditvalue,
        dayFirstOfInstalment = dayFirstInstallment,
        numberOfInstalments = numberInstallments,
        customer_id = customer_id
    )
}