package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class LogoutUser @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    UseCase<None, None>() {
    override suspend fun run(params: None): Either<Failure, None> {
        return authenticationRepository.logout()
    }
}