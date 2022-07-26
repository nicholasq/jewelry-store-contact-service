package xyz.nicholasq.contact.service.infrastructure

import com.mongodb.client.result.DeleteResult
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Singleton
import org.bson.Document
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import reactor.core.publisher.Mono
import xyz.nicholasq.contact.service.domain.contact.ContactEntity

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseMongoContainerTest : TestPropertyProvider {

}

@Singleton
open class MongoDbConnection(
    mongoConf: MongoDbConfiguration,
    mongoClient: MongoClient
) {

    private val collection: MongoCollection<ContactEntity> = mongoClient.getDatabase(mongoConf.name)
        .getCollection(mongoConf.collection, ContactEntity::class.java)

    fun cleanDb(): Mono<DeleteResult> {
        return Mono.from(collection.deleteMany(Document()))
    }
}

// todo: might need to make this into a bean
object MongoDbContainer {

    var mongoDBContainer: MongoDBContainer? = null

    fun startMongoDb() {
        if (mongoDBContainer == null) {
            // todo: parameterize image tag
            mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
                .withExposedPorts(27017)
        }
        if (!mongoDBContainer!!.isRunning) {
            mongoDBContainer!!.start()
        }
    }

    val mongoDbUri: String
        get() {
            if (mongoDBContainer == null || !mongoDBContainer!!.isRunning) {
                startMongoDb()
            }
            return mongoDBContainer!!.replicaSetUrl
        }

    fun closeMongoDb() {
        if (mongoDBContainer != null) {
            mongoDBContainer!!.close()
        }
    }
}
