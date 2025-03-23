package com.annalech.budgetcalendar.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.annalech.budgetcalendar.data.BudgetDataBase
import com.annalech.budgetcalendar.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBudgetDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        BudgetDataBase::class.java,
        Constants.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideProfileDao(db:BudgetDataBase)=
            db.getProfileDao()

    @Provides
    @Singleton
    fun proovideBudgetDao(db: BudgetDataBase) =
        db.getBudgetDao()



}