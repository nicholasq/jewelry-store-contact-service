package xyz.nicholasq.contact.service.api

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.slf4j.LoggerFactory
import xyz.nicholasq.contact.service.client.ContactClient
import xyz.nicholasq.contact.service.domain.contact.Contact
import xyz.nicholasq.contact.service.infrastructure.MongoDbConnection
import xyz.nicholasq.contact.service.infrastructure.MongoDbContainer.closeMongoDb
import xyz.nicholasq.contact.service.infrastructure.MongoDbContainer.mongoDbUri
import xyz.nicholasq.contact.service.infrastructure.MongoDbContainer.startMongoDb

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ContactControllerTest : TestPropertyProvider {

    private val log = LoggerFactory.getLogger(ContactControllerTest::class.java)

    @Inject
    lateinit var contactClient: ContactClient

    @Inject
    lateinit var mongoDbConnection: MongoDbConnection

    @BeforeAll
    fun init() {
        startMongoDb()
        mongoDbConnection.cleanDb().subscribe { log.debug("cleanup() - deleted: {}", it.deletedCount) }
    }

    @AfterAll
    fun cleanup() {
        closeMongoDb()
        mongoDbConnection.cleanDb().subscribe { log.debug("cleanup() - deleted: {}", it.deletedCount) }
    }

    override fun getProperties(): Map<String, String> {
        return mapOf("mongodb.uri" to mongoDbUri)
    }

    private var firstContact: Contact? = null


    @Test
    @Order(1)
    fun checkDbIsClean() {
        val contacts = contactClient.list()
        contacts.forEach { log.debug("checkDbIsClean() - contact: {}", it) }
        assertEquals(0, contacts.size)
    }

    @Test
    @Order(2)
    fun createContact() {
        val contact = Contact(
            id = null,
            name = "John Doe",
            email = null,
            phone = null,
            address = null,
            company = null,
            jobTitle = null,
            notes = null
        )
        firstContact = contactClient.create(contact)
        assertNotEquals(contact, firstContact)
    }

    @Test
    @Order(3)
    fun findContact() {
        val foundContact = contactClient.find(firstContact!!.id!!)
        assertEquals(firstContact, foundContact)
    }

    @Test
    @Order(4)
    fun updateContact() {
        val toUpdateContact = firstContact!!.copy(name = "Jane Doe", email = "jd@hello.com")
        val updatedContact = contactClient.update(firstContact!!.id!!, toUpdateContact)
        assertEquals(toUpdateContact, updatedContact)
    }

    @Test
    @Order(5)
    fun deleteContact() {
        val deletedContactStatus = contactClient.delete(firstContact!!.id!!)
        assertEquals(HttpStatus.NO_CONTENT, deletedContactStatus.status)
        val contactList = contactClient.list()
        assertEquals(0, contactList.size)
    }

    @Test
    @Order(6)
    fun listContacts() {
        val first = Contact(
            id = null,
            name = "Jane Doe",
            email = null,
            phone = null,
            address = null,
            company = null,
            jobTitle = null,
            notes = null
        )

        val second = first.copy(name = "Jane Doe")
        contactClient.create(first)
        contactClient.create(second)

        val contactList = contactClient.list()
        assertEquals(2, contactList.size)
    }

}
