package xyz.nicholasq.contact.service.domain.contact

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.result.InsertOneResult
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.context.annotation.Primary
import jakarta.inject.Singleton
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.findOne
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import xyz.nicholasq.contact.service.infrastructure.FirestoreConfiguration
import xyz.nicholasq.contact.service.infrastructure.MongoDbConfiguration
import javax.validation.Valid

interface ContactRepository {
    fun findById(id: String): Publisher<ContactEntity>
    fun findAll(): Publisher<ContactEntity>
    fun create(@Valid contact: ContactEntity): Publisher<InsertOneResult>
    fun update(@Valid contact: ContactEntity): Publisher<ContactEntity>
    fun delete(id: String): Publisher<Boolean>
}

@Singleton
@Primary
open class MongoDbContactRepository(
    mongoConf: MongoDbConfiguration,
    mongoClient: MongoClient
) : ContactRepository {

    private val collection: MongoCollection<ContactEntity> = mongoClient.getDatabase(mongoConf.name)
        .getCollection(mongoConf.collection, ContactEntity::class.java)

    override fun findById(id: String): Publisher<ContactEntity> {
        return collection.findOne(ContactEntity::id eq ObjectId(id))
    }

    override fun findAll(): Publisher<ContactEntity> {
        return collection.find()
    }

    override fun create(contact: ContactEntity): Publisher<InsertOneResult> {
        return collection.insertOne(contact)
    }

    override fun update(contact: ContactEntity): Publisher<ContactEntity> {
        return Mono.from(collection.findOneAndReplace(eq("_id", contact.id), contact))
            .flatMap { Mono.from(collection.find(eq("_id", it.id)).first()) }
    }

    override fun delete(id: String): Publisher<Boolean> {
        return Mono.from(collection.deleteOne(eq("_id", ObjectId(id))))
            .map { it.deletedCount > 0L }
    }
}

open class FirestoreContactRepository(
    firestoreConf: FirestoreConfiguration
) : ContactRepository {

    private val firestore: Firestore
    private val collection: String

    init {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setDatabaseUrl(firestoreConf.url)
            .build()
        FirebaseApp.initializeApp(options)
        firestore = FirestoreClient.getFirestore()
        collection = firestoreConf.collection
    }

    override fun findById(id: String): Publisher<ContactEntity> {
        TODO("Not yet implemented")
    }

    override fun findAll(): Publisher<ContactEntity> {
        TODO("Not yet implemented")
    }

    override fun create(contact: ContactEntity): Publisher<InsertOneResult> {
        TODO("Not yet implemented")
    }

    override fun update(contact: ContactEntity): Publisher<ContactEntity> {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): Publisher<Boolean> {
        TODO("Not yet implemented")
    }
}
