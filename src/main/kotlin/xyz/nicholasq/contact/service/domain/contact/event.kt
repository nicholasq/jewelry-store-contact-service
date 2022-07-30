package xyz.nicholasq.contact.service.domain.contact

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

interface ContactEventService {
    suspend fun create(contactEvent: ContactEvent)
    suspend fun update(contactEvent: ContactEvent)
    suspend fun delete(contactEvent: ContactEvent)
}

@Singleton
open class PubsubContactEventService : ContactEventService {

    private val log = LoggerFactory.getLogger(PubsubContactEventService::class.java)

    override suspend fun create(contactEvent: ContactEvent) {
        log.debug("create: {}", contactEvent)
    }

    override suspend fun update(contactEvent: ContactEvent) {
        log.debug("update: {}", contactEvent)
    }

    override suspend fun delete(contactEvent: ContactEvent) {
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
