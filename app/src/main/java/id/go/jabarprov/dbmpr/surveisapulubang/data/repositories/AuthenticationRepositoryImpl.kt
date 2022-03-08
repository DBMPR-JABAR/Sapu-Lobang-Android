package id.go.jabarprov.dbmpr.surveisapulubang.data.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toError
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toSuccess
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.RemoteDataSourceFailure
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth.AuthLocalDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.auth.AuthRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.UserDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) :
    AuthenticationRepository {
    override suspend fun login(username: String, password: String): Either<Failure, User> {
        return try {
            val result = authRemoteDataSource.login(username, password)
            val user = UserDataMapper.convertLoginDataResponseToEntity(result)
            authLocalDataSource.storeToken(user.token)
            user.toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun logout(): Either<Failure, None> {
        authLocalDataSource.clearToken()
        return None.toSuccess()
    }
}