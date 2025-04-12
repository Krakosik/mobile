package com.pawlowski.network.service

import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import events.EventServiceGrpcKt
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EventsServiceProvider
    @Inject
    constructor(
        private val getGrpcChannelUseCase: IGetGrpcChannelUseCase,
    ) : IEventsServiceProvider {
        private var service: EventServiceGrpcKt.EventServiceCoroutineStub? = null

        private val mutex = Mutex()

        override suspend operator fun invoke(): EventServiceGrpcKt.EventServiceCoroutineStub =
            mutex.withLock {
                getGrpcChannelUseCase(
                    url = "node01.solidchat.io",
                    port = 3001,
                ).let { channel ->
                    EventServiceGrpcKt.EventServiceCoroutineStub(channel)
                }.also {
                    service = it
                }
            }
    }
