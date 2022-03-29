package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.UserData
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LoginResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Role
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.addSeconds
import java.util.*

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
                internalRole = Role.convertStringToRole(userResponse.internalRole.role.split(" - ")[0]),
                sup = userResponse.sup,
                idSup = userResponse.supId,
                uptd = userResponse.internalRole.uptd,
                idUptd = userResponse.uptdId,
                ruasJalan = RuasDataMapper.convertListOfRuasDataResponseToListOfEntity(userResponse.ruas),
                token = loginResponse.token.accessToken,
                tokenExpiredDate = Calendar.getInstance().addSeconds(loginResponse.token.expiresIn)
            )
        }

        fun convertUserDataToEntity(userData: UserData): User {
            return User(
                id = userData.id,
                nama = userData.nama,
                email = userData.email,
                role = userData.role,
                idInternalRole = userData.idInternalRole,
                internalRole = Role.convertStringToRole(userData.internalRole),
                sup = userData.sup,
                idSup = userData.idSup,
                uptd = userData.uptd,
                idUptd = userData.idUptd,
                ruasJalan = userData.ruasJalan,
                token = userData.token,
                tokenExpiredDate = userData.tokenExpiredDate
            )
        }

        fun convertEntityToUserData(user: User): UserData {
            return UserData(
                id = user.id,
                nama = user.nama,
                email = user.email,
                role = user.role,
                idInternalRole = user.idInternalRole,
                internalRole = user.internalRole.convertToString(),
                sup = user.sup,
                idSup = user.idSup,
                uptd = user.uptd,
                idUptd = user.idUptd,
                ruasJalan = user.ruasJalan,
                token = user.token,
                tokenExpiredDate = user.tokenExpiredDate
            )
        }
    }
}