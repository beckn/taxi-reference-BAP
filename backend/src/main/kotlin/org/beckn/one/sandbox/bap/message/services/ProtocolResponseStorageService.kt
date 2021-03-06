package org.beckn.one.sandbox.bap.message.services

import arrow.core.Either
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.BecknResponseDao
import org.beckn.one.sandbox.bap.message.mappers.ProtocolGenericResponseMapper
import org.beckn.one.sandbox.bap.message.repositories.BecknProtocolResponseRepository
import org.beckn.protocol.schemas.ProtocolResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface ProtocolResponseStorageService<Proto : ProtocolResponse> {
  fun save(protoResponse: Proto): Either<DatabaseError.OnWrite, Proto>
  fun findByMessageId(id: String): Either<DatabaseError.OnRead, List<Proto>>
  fun findByOrderId(id: String): Either<DatabaseError.OnRead, List<Proto>>
  fun findSearchCatalog(id: String, providerName: String?, categoryName: String?): Either<DatabaseError.OnRead, List<Proto>>
}

class ProtocolResponseStorageServiceImpl<Proto : ProtocolResponse, Entity : BecknResponseDao> constructor(
  val responseRepo: BecknProtocolResponseRepository<Entity>,
  val mapper: ProtocolGenericResponseMapper<Proto, Entity>
) : ProtocolResponseStorageService<Proto> {
  private val log: Logger = LoggerFactory.getLogger(this::class.java)

  override fun save(protoResponse: Proto): Either<DatabaseError.OnWrite, Proto> =
    Either
      .catch {
        log.info("Saving protocol response: {}", protoResponse)
        log.info("Saving protocol mapped response: {}",mapper.protocolToEntity(protoResponse) )
        responseRepo.insertOne(mapper.protocolToEntity(protoResponse))
      }
      .bimap(
        rightOperation = { protoResponse },
        leftOperation = {
          log.error("Exception while saving search response", it)
          DatabaseError.OnWrite
        }
      )

  override fun findByMessageId(id: String): Either<DatabaseError.OnRead, List<Proto>> = Either
    .catch {
      responseRepo.findByMessageId(id)
    }
    .map {
      toSchema(it)
    }
    .mapLeft { e ->
      log.error("Exception while fetching search response", e)
      DatabaseError.OnRead
    }

  private fun toSchema(allResponses: List<Entity>) =
    allResponses.map { response -> mapper.entityToProtocol(response) }

  override fun findByOrderId(id: String): Either<DatabaseError.OnRead, List<Proto>> = Either
  .catch { responseRepo.findByOrderId(id) }
  .map {
    toSchema(it) }
  .mapLeft { e ->
    log.error("Exception while fetching search response", e)
    DatabaseError.OnRead
  }

  override fun findSearchCatalog(id: String, providerName: String?, categoryName: String?): Either<DatabaseError.OnRead, List<Proto>> = Either
    .catch {
      if (providerName != null) {
        log.info("Polling Search By provider name initiated")
        responseRepo.findByProviderName(id, providerName)
      } else if (categoryName != null) {
        log.info("Polling Search By category name initiated")
        responseRepo.findByCategoryName(id = id, categoryName= categoryName)
      } else {
        log.info("Polling Search By item initiated")
        responseRepo.findByMessageId(id)
      }
    }
    .map { toSchema(it) }
    .mapLeft { e ->
      log.error("Exception while fetching search response", e)
      DatabaseError.OnRead
    }

}