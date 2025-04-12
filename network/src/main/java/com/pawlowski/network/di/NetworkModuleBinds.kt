package com.pawlowski.network.di

import com.pawlowski.network.IEventsDataProvider
import com.pawlowski.network.channel.GetGrpcChannelUseCase
import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import com.pawlowski.network.dataProvider.EventsDataProvider
import com.pawlowski.network.service.EventsServiceProvider
import com.pawlowski.network.service.IEventsServiceProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModuleBinds {
    @Binds
    abstract fun getGrpcChannelUseCase(getGrpcChannelUseCase: GetGrpcChannelUseCase): IGetGrpcChannelUseCase

    @Binds
    abstract fun eventsServiceProvider(eventsServiceProvider: EventsServiceProvider): IEventsServiceProvider

    @Binds
    abstract fun eventsDataProvider(eventsDataProvider: EventsDataProvider): IEventsDataProvider
}
