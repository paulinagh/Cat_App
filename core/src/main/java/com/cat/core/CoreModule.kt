package com.cat.core

import androidx.room.Room
import com.cat.core.local.LocalDataSource
import com.cat.core.local.room.CatDatabase
import com.cat.core.remote.APIService
import com.cat.core.remote.AuthInterceptor
import com.cat.core.repository.CatRepository
import com.cat.core.repository.ICatRepository
import com.cat.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<CatDatabase>().catDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("cat_app".toCharArray())
        val factory = SupportFactory(passphrase)

        Room.databaseBuilder(
            androidContext(),
            CatDatabase::class.java, "Cat.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.thecatapi.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()

        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient(BuildConfig.API_KEY))
            .build()
        retrofit.create(APIService::class.java)
    }
}

private fun okhttpClient(key: String): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor(key))
    .build()

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<ICatRepository> { CatRepository(get(), get(), get()) }
}