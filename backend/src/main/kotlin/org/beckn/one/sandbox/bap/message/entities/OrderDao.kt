package org.beckn.one.sandbox.bap.message.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.beckn.protocol.schemas.*

data class OrderDao @Default constructor(
  val provider: SelectMessageSelectedProviderDao? = null,
  val items: List<SelectMessageSelectedItemsDao>? = null,
  val addOns: List<SelectMessageSelectedAddOnsDao>? = null,
  val offers: List<SelectMessageSelectedOffersDao>? = null,
  val billing: BillingDao? = null,
  val fulfillment: FulfillmentDao? = null,
  val quote: QuotationDao? = null,
  val payment: PaymentDao? = null,
  val id: String? = null,
  val state: String? = null,
  val createdAt: java.time.OffsetDateTime? = null,
  val updatedAt: java.time.OffsetDateTime? = null,
  @field:JsonIgnore
  override val context: ContextDao?=null,
  @field:JsonIgnore
  override val error: ErrorDao?= null,
  override var userId: String?,
  var transactionId: String? = null,
  var messageId: String? = null,
  var parentOrderId:String? = null,
  val documents: DocumentDao? = null,
):BecknResponseDao


data class SelectMessageSelectedProviderDao @Default constructor(
  val id: String,
  val locations: List<SelectMessageSelectedProviderLocationsDao>?
)

data class SelectMessageSelectedProviderLocationsDao @Default constructor(
  val id: String
)

// TODO can be common
data class SelectMessageSelectedAddOnsDao @Default constructor(
  val id: String
)

// TODO similar to OnInitMessageInitializedItems
data class SelectMessageSelectedItemsDao @Default constructor(
  val id: String? = null,
  val quantity: ItemQuantityAllocatedDao? = null,
  val fulfillmentId: String? = null,
  val categoryId: String? = null,
  val descriptor: DescriptorDao? = null,
  )

data class SelectMessageSelectedOffersDao @Default constructor(
  val id: String
)

data class SelectedReasonDao @Default constructor(
  val id: String
)

data class PolicyDao @Default constructor(
  val id: String?= null,
  val parentPolicyId: String?= null,
  val descriptor: DescriptorDao? = null,
  val time: TimeDao? = null,
)

data class OptionDao  @Default constructor(
  val id: String? = null,
  val descriptor: DescriptorDao? = null,
)
