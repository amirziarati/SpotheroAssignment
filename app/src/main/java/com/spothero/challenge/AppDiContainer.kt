package com.spothero.challenge

import android.content.Context
import com.spothero.challenge.data.SpotHeroApi


/**
 * app level di.
 */
interface AppDiContainer {
    val spotHeroApi : SpotHeroApi
}

class AppDiContainerImpl(private val applicationContext: Context) : AppDiContainer {

    override val spotHeroApi: SpotHeroApi by lazy {
        SpotHeroApi(applicationContext)
    }

}
