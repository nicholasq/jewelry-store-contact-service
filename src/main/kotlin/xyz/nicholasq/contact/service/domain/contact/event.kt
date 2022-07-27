package xyz.nicholasq.contact.service.domain.contact

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

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
