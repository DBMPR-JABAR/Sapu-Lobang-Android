package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LoginResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User

abstract class UserDataMapper {
    companion object {
        fun convertLoginDataResponseToEntity(loginResponse: LoginResponse): User {
            val userResponse = loginResponse.user
            return User(
                id = userResponse.id,
                nama = userResponse.name,
                email = userResponse.email,
                role = userResponse.role,
                idInternalRole = userResponse.internalRoleId,
                internalRole = userResponse.internalRole.role,
                sup = userResponse.sup,
                idSup = userResponse.supId,
                uptd = userResponse.internalRole.uptd,
                idUptd = userResponse.uptdId,
                ruasJalan = RuasDataMapper.convertListOfRuasDataResponseToListOfEntity(userResponse.ruas),
                token = loginResponse.token.accessToken
            )
        }
    }
}