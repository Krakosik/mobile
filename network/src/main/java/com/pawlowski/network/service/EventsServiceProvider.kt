package com.pawlowski.network.service

import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import com.pawlowski.network.utils.addTokenHeader
import events.EventServiceGrpcKt
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
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
            mutex
                .withLock {
                    service ?: getGrpcChannelUseCase(
                        url = "node01.solidchat.io",
                        port = 3001,
                    ).let { channel ->
                        EventServiceGrpcKt.EventServiceCoroutineStub(channel)
                    }.also {
                        service = it
                    }.addTokenHeader(
                        token =
                            FirebaseAuth
                                .getInstance()
                                .currentUser
                                ?.getIdToken(false)
                                ?.await()
                                ?.token
                                .also { println("Token: $it") } ?: "",
                    )
                }
    }
