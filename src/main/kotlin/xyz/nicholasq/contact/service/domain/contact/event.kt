package xyz.nicholasq.contact.service.domain.contact

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.*

interface ContactEventService {
    fun create(contactEvent: ContactEvent)
    fun update(contactEvent: ContactEvent)
    fun delete(contactEvent: ContactEvent)
}

@Singleton
open class PubsubContactEventService : ContactEventService {

    private val log = LoggerFactory.getLogger(PubsubContactEventService::class.java)

    override fun create(contactEvent: ContactEvent) {
        log.debug("create: {}", contactEvent)
    }

    override fun update(contactEvent: ContactEvent) {
        log.debug("update: {}", contactEvent)
    }

    override fun delete(contactEvent: ContactEvent) {
        log.debug("delete: {}", contactEvent)
    }
}

data class ContactEvent(
    val id: String,
    val contactId: String,
    val eventType: ContactEventType,
    val eventDate: LocalDateTime,
    val eventDescription: String
)

enum class ContactEventType {
    CREATE, UPDATE, DELETE
}

fun Contact.toContactEvent(contactEventType: ContactEventType): ContactEvent {
    return ContactEvent(
        id = UUID.randomUUID().toString(),
        contactId = id!!,
        eventType = contactEventType,
        eventDate = LocalDateTime.now(),
        eventDescription = "Contact ${contactEventType.name.lowercase()}"
    )
}

fun deleteContactEvent(id: String): ContactEvent {
    return ContactEvent(
        id = UUID.randomUUID().toString(),
        contactId = id,
        eventType = ContactEventType.DELETE,
        eventDate = LocalDateTime.now(),
        eventDescription = "Contact ${ContactEventType.DELETE.name.lowercase()}"
    )
}
