package com.pawlowski.network.service

import ElectrocardiogramGrpcKt
import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EkgServiceProvider
    @Inject
    constructor(
        private val getGrpcChannelUseCase: IGetGrpcChannelUseCase,
    ) : IEkgServiceProvider {
        private var service: ElectrocardiogramGrpcKt.ElectrocardiogramCoroutineStub? = null

        private val mutex = Mutex()

        override suspend operator fun invoke(): ElectrocardiogramGrpcKt.ElectrocardiogramCoroutineStub =
            mutex.withLock {
                getGrpcChannelUseCase(
                    url = "grpc://node01.solidchat.io",
                    port = 3001,
                ).let { channel ->
                    ElectrocardiogramGrpcKt.ElectrocardiogramCoroutineStub(channel)
                }.also {
                    service = it
                }
            }
    }
