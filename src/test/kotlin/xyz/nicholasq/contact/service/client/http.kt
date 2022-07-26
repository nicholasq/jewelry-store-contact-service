package xyz.nicholasq.contact.service.client

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import xyz.nicholasq.contact.service.domain.contact.Contact

@Client("/api/v1/contact")
interface ContactClient {

    @Get("/{id}")
    fun find(@PathVariable id: String): Contact

    @Get("/")
    fun list(): List<Contact>

    @Post
    fun create(@Body contact: Contact): Contact

    @Put("/{id}")
    fun update(@PathVariable id: String, @Body contact: Contact): Contact

    @Delete("/{id}")
    fun delete(@PathVariable id: String): HttpResponse<Void>
}
