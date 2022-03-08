package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class LoginUser @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    UseCase<User, LoginUser.Params>() {
    override suspend fun run(params: Params): Either<Failure, User> {
        return authenticationRepository.login(params.username, params.password)
    }

    data class Params(val username: String, val password: String)
}