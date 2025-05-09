package com.pawlowski.network.utils

import io.grpc.Metadata
import io.grpc.stub.AbstractStub
import io.grpc.stub.MetadataUtils

internal fun <T : AbstractStub<T>> T.addTokenHeader(token: String): T =
    this.withInterceptors(
        MetadataUtils.newAttachHeadersInterceptor(
            Metadata().apply {
                put(
                    Metadata.Key.of(
                        "authorization",
                        Metadata.ASCII_STRING_MARSHALLER,
                    ),
                    "Bearer $token",
                )
            },
        ),
    )
