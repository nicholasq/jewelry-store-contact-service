package xyz.nicholasq.contact.service.domain.contact

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

@Introspected
data class ContactEntity @BsonCreator @Creator constructor(

    @field:BsonId @param:BsonId var id: ObjectId?,
    @field:BsonProperty("name") @param:BsonProperty("name") var name: String,
    @field:BsonProperty("email") @param:BsonProperty("email") var email: String?,
    @field:BsonProperty("phone") @param:BsonProperty("phone") var phone: String?,
    @field:BsonProperty("address") @param:BsonProperty("address") var address: String?,
    @field:BsonProperty("company") @param:BsonProperty("company") var company: String?,
    @field:BsonProperty("jobTitle") @param:BsonProperty("jobTitle") var jobTitle: String?,
    @field:BsonProperty("notes") @param:BsonProperty("notes") var notes: String?
)
