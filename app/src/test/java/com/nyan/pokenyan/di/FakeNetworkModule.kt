package com.nyan.pokenyan.di

import com.nyan.data.di.NetworkModule
import com.nyan.pokenyan.utils.MockServer
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
class FakeNetworkModule: NetworkModule() {

    override fun baseUrl(): HttpUrl {
        return MockWebServer().url("/")
    }

}