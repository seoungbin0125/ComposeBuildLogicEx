package com.bluebirdcorp.softpos.repository.di

import android.content.Context
import com.bluebirdcorp.softpos.datasource.dao.BarcodeDao
import com.bluebirdcorp.softpos.domain.interfaces.BarcodeDBRepo
import com.bluebirdcorp.softpos.domain.interfaces.BarcodeHandelRepo
import com.bluebirdcorp.softpos.domain.item.BarcodeItem
import com.bluebirdcorp.softpos.repository.BarcodeDBRepoImpl
import com.bluebirdcorp.softpos.repository.BarcodeHandleRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBarcodeHandleRepo(
        @ApplicationContext context: Context
    ): BarcodeHandelRepo {
        return BarcodeHandleRepoImpl(context)
    }

    @Provides
    @Singleton
    fun provideBarcodeDBRepo(
        @ApplicationContext context: Context,
        barcodeDao: BarcodeDao
    ): BarcodeDBRepo<BarcodeItem> {
        return BarcodeDBRepoImpl(context, barcodeDao)
    }
}
