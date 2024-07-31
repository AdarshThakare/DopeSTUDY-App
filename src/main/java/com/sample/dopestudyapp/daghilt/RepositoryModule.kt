package com.sample.dopestudyapp.daghilt

import com.sample.dopestudyapp.data.repository.SessionsRepository
import com.sample.dopestudyapp.data.repository.SubjectRepository
import com.sample.dopestudyapp.data.repository.TaskRepository
import com.sample.dopestudyapp.database.repositoryImplementation.SessionsRepositoryImplementation
import com.sample.dopestudyapp.database.repositoryImplementation.SubjectRepositoryImplementation
import com.sample.dopestudyapp.database.repositoryImplementation.TaskRepositoryImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // binding the repositories to their respective implementations
    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        impl: SubjectRepositoryImplementation
    ): SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl : TaskRepositoryImplementation
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl : SessionsRepositoryImplementation
    ): SessionsRepository

}