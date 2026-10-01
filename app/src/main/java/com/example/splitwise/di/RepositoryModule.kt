package com.example.splitwise.di
import com.example.splitwise.data.repositories.ExpenseRepository
import com.example.splitwise.data.repositories.ExpenseRepositoryImpl
import com.example.splitwise.data.repositories.FriendRepository
import com.example.splitwise.data.repositories.FriendRepositoryImpl
import com.example.splitwise.data.repositories.GroupRepository
import com.example.splitwise.data.repositories.GroupRepositoryImpl
import com.example.splitwise.data.repositories.UserRepository
import com.example.splitwise.data.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGroupRepository(
        implementation: GroupRepositoryImpl
    ): GroupRepository
    @Binds
    @Singleton
    abstract fun bindFriendRepository(
        implementation: FriendRepositoryImpl
    ): FriendRepository
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        implementation: ExpenseRepositoryImpl
    ): ExpenseRepository
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        implementation: UserRepositoryImpl
    ): UserRepository
}