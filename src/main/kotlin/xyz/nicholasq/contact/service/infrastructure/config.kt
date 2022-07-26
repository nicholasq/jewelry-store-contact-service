package xyz.nicholasq.contact.service.infrastructure

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.naming.Named

@ConfigurationProperties("db")
interface MongoDbConfiguration : Named {

    val collection: String
}

@ConfigurationProperties("firestore.db")
interface FirestoreConfiguration : Named {

    val collection: String
    val url: String
}
