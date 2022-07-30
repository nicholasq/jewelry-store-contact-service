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
import kotlinx.coroutines.reactive.awaitFirst
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.findOne
import xyz.nicholasq.contact.service.infrastructure.FirestoreConfiguration
import xyz.nicholasq.contact.service.infrastructure.MongoDbConfiguration
import javax.validation.Valid

interface ContactRepository {
    suspend fun findById(id: String): ContactEntity
    suspend fun findAll(): List<ContactEntity>
    suspend fun save(@Valid contact: ContactEntity): InsertOneResult
    suspend fun update(@Valid contact: ContactEntity): ContactEntity
    suspend fun delete(id: String): Boolean
}

@Singleton
@Primary
open class MongoDbContactRepository(
    mongoConf: MongoDbConfiguration,
    mongoClient: MongoClient
) : ContactRepository {

    private val collection: MongoCollection<ContactEntity> = mongoClient.getDatabase(mongoConf.name)
        .getCollection(mongoConf.collection, ContactEntity::class.java)

    override suspend fun findById(id: String): ContactEntity {
        return collection.findOne(ContactEntity::id eq ObjectId(id)).awaitFirst()
    }

    override suspend fun findAll(): List<ContactEntity> {
        return collection.find().toList()
    }

    override suspend fun save(contact: ContactEntity): InsertOneResult {
        return collection.insertOne(contact).awaitFirst()
    }

    override suspend fun update(contact: ContactEntity): ContactEntity {
        // Apparently it can return the object before or after the update. It just feels safer to
        // update then fetch and return that.
        val returned = collection.findOneAndReplace(eq("_id", contact.id), contact).awaitFirst()
        return findById(returned.id.toString())
    }

    override suspend fun delete(id: String): Boolean {
        val deletedCount = collection.deleteOne(eq("_id", ObjectId(id))).awaitFirst().deletedCount
        return deletedCount > 0L
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

    override suspend fun findById(id: String): ContactEntity {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): List<ContactEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun save(contact: ContactEntity): InsertOneResult {
        TODO("Not yet implemented")
    }

    override suspend fun update(contact: ContactEntity): ContactEntity {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): Boolean {
        TODO("Not yet implemented")
    }
}
