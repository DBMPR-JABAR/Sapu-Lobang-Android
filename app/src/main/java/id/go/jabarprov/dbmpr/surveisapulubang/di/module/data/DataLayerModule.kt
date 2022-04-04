package id.go.jabarprov.dbmpr.surveisapulubang.di.module.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth.AuthLocalDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth.AuthLocalDataSourceImpl
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.auth.AuthRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.auth.AuthRemoteDataSourceImpl
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan.PenangananRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan.PenangananRemoteDataSourceImpl
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rekapitulasi.RekapitulasiRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rekapitulasi.RekapitulasiRemoteDataSourceImpl
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana.RencanaRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana.RencanaRemoteDataSourceImpl
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang.SurveiLubangRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang.SurveiLubangRemoteDataSourceImpl
import id.go.jabarprov.dbmpr.surveisapulubang.data.repositories.*
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DataLayerModule {
    @Binds
    abstract fun bindAuthLocalDataSource(authLocalDataSourceImpl: AuthLocalDataSourceImpl): AuthLocalDataSource

    @Binds
    abstract fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    abstract fun bindSurveiLubangRemoteDataSource(surveiLubangRemoteDataSourceImpl: SurveiLubangRemoteDataSourceImpl): SurveiLubangRemoteDataSource

    @Binds
    abstract fun bindSurveiLubangRepository(surveiLubangRepositoryImpl: SurveiLubangRepositoryImpl): SurveiLubangRepository

    @Binds
    abstract fun bindRencanaRemoteDataSource(rencanaRemoteDataSourceImpl: RencanaRemoteDataSourceImpl): RencanaRemoteDataSource

    @Binds
    abstract fun bindRencanaRepository(rencanaRepositoryImpl: RencanaRepositoryImpl): RencanaRepository

    @Binds
    abstract fun bindPenangananRemoteDataSource(penangananRemoteDataSourceImpl: PenangananRemoteDataSourceImpl): PenangananRemoteDataSource

    @Binds
    abstract fun bindPenangananRepository(penangananRepositoryImpl: PenangananRepositoryImpl): PenangananRepository

    @Binds
    abstract fun bindRekapitulasiRemoteDataSource(rekapitulasiRemoteDataSourceImpl: RekapitulasiRemoteDataSourceImpl): RekapitulasiRemoteDataSource

    @Binds
    abstract fun bindRekapitulasiRepository(rekapitulasiRepositoryImpl: RekapitulasiRepositoryImpl): RekapitulasiRepository
}