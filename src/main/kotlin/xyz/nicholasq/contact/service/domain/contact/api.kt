package xyz.nicholasq.contact.service.domain.contact

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

@Controller("/api/v1/contact")
open class ContactController(private val contactService: ContactService) {

    private val log = LoggerFactory.getLogger(ContactController::class.java)

    @Post("/")
    @Status(HttpStatus.CREATED)
    open suspend fun create(@Body contact: Contact): Contact {
        log.debug("create() - contact: $contact")
        return contactService.create(contact)
    }

    @Get("/{id}")
    @Status(HttpStatus.OK)
    open suspend fun findById(@PathVariable("id") id: String): Contact {
        log.debug("findById() - id: $id")
        return contactService.findById(id)
    }

    @Put("/{id}")
    @Status(HttpStatus.OK)
    open suspend fun update(@PathVariable id: String, @Body contact: Contact): Contact {
        log.debug("update() - id: $id, contact: $contact")
        return contactService.update(contact)
    }

    @Delete("/{id}")
    open suspend fun delete(@PathVariable("id") id: String): HttpResponse<Void> {
        log.debug("delete() - id: $id")
        val deleted = contactService.delete(id)
        return if (deleted) HttpResponse.noContent() else HttpResponse.notFound()
    }

    @Get("/")
    @Status(HttpStatus.OK)
    open suspend fun findAll(): List<Contact> {
        log.debug("findAll()")
        return contactService.findAll()
    }
}
