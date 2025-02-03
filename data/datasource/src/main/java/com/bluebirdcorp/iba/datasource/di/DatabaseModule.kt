package com.bluebirdcorp.iba.datasource.di

import android.content.Context
import com.bluebirdcorp.iba.datasource.BarcodeDatabase
import com.bluebirdcorp.iba.datasource.dao.BarcodeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBarcodeDatabase(
        @ApplicationContext appContext: Context
    ): BarcodeDatabase = BarcodeDatabase.getDatabase(appContext)

    @Provides
    fun provideBarcodeDao(database: BarcodeDatabase): BarcodeDao {
        return database.barcodeDao()
    }
}
