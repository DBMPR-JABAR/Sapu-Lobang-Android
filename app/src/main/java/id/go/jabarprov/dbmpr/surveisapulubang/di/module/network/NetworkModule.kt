package id.go.jabarprov.dbmpr.surveisapulubang.di.module.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth.AuthLocalDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.*
import id.go.jabarprov.dbmpr.surveisapulubang.di.module.qualifier.RetrofitAuthorized
import id.go.jabarprov.dbmpr.surveisapulubang.di.module.qualifier.RetrofitUnauthorized
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    companion object {
        @Provides
        @RetrofitUnauthorized
        fun providesRetrofitUnauthorizedInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://tj.temanjabar.net/api/")
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(
                            HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                        )
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @RetrofitAuthorized
        fun providesRetrofitAuthorizedInstance(authLocalDataSource: AuthLocalDataSource): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://tj.temanjabar.net/api/")
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor { chain ->
                            val requestBuilder = chain.request().newBuilder()
                            requestBuilder.addHeader(
                                "Authorization",
                                "Bearer ${authLocalDataSource.getToken()}"
                            )
                            chain.proceed(requestBuilder.build())
                        }
                        .addInterceptor(
                            HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                        )
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun providesAuthAPIInstance(@RetrofitUnauthorized retrofit: Retrofit): AuthAPI {
            return retrofit.create(AuthAPI::class.java)
        }

        @Provides
        fun providesSurveiLubangAPIInstance(@RetrofitAuthorized retrofit: Retrofit): SurveiLubangAPI {
            return retrofit.create(SurveiLubangAPI::class.java)
        }

        @Provides
        fun providesPenangananAPIInstance(@RetrofitAuthorized retrofit: Retrofit): PenangananAPI {
            return retrofit.create(PenangananAPI::class.java)
        }

        @Provides
        fun providesRencanaAPIInstance(@RetrofitAuthorized retrofit: Retrofit): RencanaAPI {
            return retrofit.create(RencanaAPI::class.java)
        }

        @Provides
        fun providesRekapAPIInstance(@RetrofitAuthorized retrofit: Retrofit): RekapAPI {
            return retrofit.create(RekapAPI::class.java)
        }
    }
}