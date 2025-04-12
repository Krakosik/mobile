package com.pawlowski.network.service

import events.EventServiceGrpcKt

interface IEventsServiceProvider {
    suspend operator fun invoke(): EventServiceGrpcKt.EventServiceCoroutineStub
}
