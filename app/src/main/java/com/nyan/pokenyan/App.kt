package com.nyan.pokenyan

import android.app.Application
import android.util.Log
import com.nyan.data.di.NetworkModule.networkModule
import com.nyan.data.di.RepositoryModule.repositoryModule
import com.nyan.domain.di.DomainModule.domainModule
import com.nyan.pokenyan.di.PresentationModule.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        //Initialized timber.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        //Initialized Koin modules.
        startKoin {
            androidContext(this@App)
            modules(presentationModule, networkModule, repositoryModule, domainModule)
        }

    }

    private class ReleaseTree : Timber.Tree() {
        override fun log(
            priority: Int, tag: String?, message: String,
            t: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            // log your crash to your favourite
            // Sending crash report to Firebase CrashAnalytics

            // FirebaseCrash.report(message);
            // FirebaseCrash.report(new Exception(message));
        }
    }

}