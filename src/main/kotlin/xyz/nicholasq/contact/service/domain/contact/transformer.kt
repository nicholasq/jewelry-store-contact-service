package xyz.nicholasq.contact.service.domain.contact

import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.*

fun ContactEntity.toContact(): Contact {
    return with(this) {
        Contact(
            id.toString(),
            name,
            email,
            phone,
            address,
            company,
            jobTitle,
            notes
        )
    }
}

fun Contact.toContactEntity(): ContactEntity {
    return with(this) {
        ContactEntity(
            if (id != null) ObjectId(id) else null,
            name,
            email,
            phone,
            address,
            company,
            jobTitle,
            notes
        )
    }
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
