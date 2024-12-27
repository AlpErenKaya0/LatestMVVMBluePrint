package com.example.latestmvvmblueprint.di

import com.example.latestmvvmblueprint.common.Constants
import com.example.latestmvvmblueprint.data.remote.CoinPaprikaApi
import com.example.latestmvvmblueprint.data.repository.CoinRepositoryImpl
import com.example.latestmvvmblueprint.domain.repository.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun provideCoinRepository(impl: CoinRepositoryImpl): CoinRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppNetworkModule {

    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaApi::class.java)
    }
}
