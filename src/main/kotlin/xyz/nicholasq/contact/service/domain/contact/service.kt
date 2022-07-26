package xyz.nicholasq.contact.service.domain.contact

import com.mongodb.client.result.InsertOneResult
import jakarta.inject.Singleton
import kotlinx.coroutines.reactive.awaitFirst
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.toList
import org.slf4j.LoggerFactory

interface ContactService {
    suspend fun findById(id: String): Contact
    suspend fun findAll(): List<Contact>
    suspend fun create(contact: Contact): Contact
    suspend fun update(contact: Contact): Contact
    suspend fun delete(id: String): Boolean
}

@Singleton
open class DefaultContactService(
    private val contactRepository: ContactRepository,
    private val contactEventService: ContactEventService
) : ContactService {

    private val log = LoggerFactory.getLogger(DefaultContactService::class.java)

    override suspend fun findById(id: String): Contact {
        log.debug("findById() - id: $id")
        return contactRepository.findById(id).awaitFirst().toContact()
    }

    override suspend fun findAll(): List<Contact> {
        log.debug("findAll()")
        return contactRepository.findAll().toList().map { it.toContact() }
    }

    override suspend fun create(contact: Contact): Contact {
        log.debug("create() - contact: $contact")
        val insertResult: InsertOneResult = contactRepository.create(contact.toContactEntity()).awaitFirst()
        val id = (insertResult.insertedId as BsonObjectId).value.toString()
        val savedContact = contactRepository.findById(id).awaitFirst().toContact()

        contactEventService.create(savedContact.toContactEvent(ContactEventType.CREATE))

        return savedContact
    }

    override suspend fun update(contact: Contact): Contact {
        log.debug("update() - contact: $contact")
        val updatedContact = contactRepository.update(contact.toContactEntity()).awaitFirst().toContact()

        contactEventService.create(updatedContact.toContactEvent(ContactEventType.UPDATE))

        return updatedContact
    }

    override suspend fun delete(id: String): Boolean {
        log.debug("delete() - id: $id")
        val deleted = contactRepository.delete(id).awaitFirst()

        contactEventService.delete(deleteContactEvent(id))

        return deleted
    }
}

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
