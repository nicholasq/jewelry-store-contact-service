package xyz.nicholasq.contact.service.domain.contact

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotEmpty

@Introspected
data class Contact constructor(
    var id: String?,
    @field:NotEmpty var name: String?,
    var email: String?,
    var phone: String?,
    var address: String?,
    var company: String?,
    var jobTitle: String?,
    var notes: String?
) {
    constructor() : this(null, null, null, null, null, null, null, null)
}
