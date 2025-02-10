package com.bluebirdcorp.softpos.di

import com.bluebirdcorp.softpos.feacture.interfaces.BarcodeScanRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBarcodeScanRepo(): BarcodeScanRepo {
        return BarcodeScanRepoImpl()
    }
}
