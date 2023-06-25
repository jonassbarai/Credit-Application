package me.dio.credit.application.system.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHeadler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidException(ex : MethodArgumentNotValidException): ResponseEntity<ExceptionsDetails>{
        val errors:MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.stream()
            .forEach{
                erro: ObjectError ->
                val fieldname: String = (erro as FieldError).field
                val messageError: String? = erro.defaultMessage
                errors[fieldname] = messageError
            }
        return ResponseEntity(
            ExceptionsDetails(
                title = "Bad request, consult the documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                details = errors
            ),HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handlerDataAcessException(ex : DataAccessException): ResponseEntity<ExceptionsDetails>{
        val errors:MutableMap<String, String?> = HashMap()
        return ResponseEntity(
            ExceptionsDetails(
                title = "Conflict, consult the documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf(ex.toString() to ex.message)
            ),HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handlerBussinessException(ex : BusinessException): ResponseEntity<ExceptionsDetails>{
        val errors:MutableMap<String, String?> = HashMap()
        return ResponseEntity(
            ExceptionsDetails(
                title = "Bad request, consult the documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf(ex.toString() to ex.message)
            ),HttpStatus.BAD_REQUEST
        )
    }
}