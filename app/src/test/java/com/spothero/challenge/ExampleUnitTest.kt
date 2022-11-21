package com.spothero.challenge

import com.spothero.challenge.data.SpotRepo
import com.spothero.challenge.data.SpotRepoImpl
import com.spothero.challenge.data.model.Address
import com.spothero.challenge.data.model.Spot
import com.spothero.challenge.data.remote.SpotHeroApi
import com.spothero.challenge.ui.spotlist.SpotListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val spotRepo = mock(SpotRepo::class.java)
    private val spotListViewModel: SpotListViewModel by lazy {
        SpotListViewModel(spotRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    private val spot1 = getDummySpot(0)
    private val spot2 = getDummySpot(10)
    private val spot3 = getDummySpot(3)
    private val spot4 = getDummySpot(1000)
    private val spots = listOf(spot1, spot2, spot3, spot4)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun spotsShouldBeOrderedByPrice() {
        `when`(spotRepo.latestSpots).thenReturn(flow { emit(spots) })

        runTest {
            val actualList =
                spotListViewModel.viewState.first { it.latestSpots.size == 4 }.latestSpots
            assert(actualList[0].price == 0L)
            assert(actualList[1].price == 3L)
            assert(actualList[2].price == 10L)
            assert(actualList[3].price == 1000L)
        }
    }

}

private fun getDummySpot(price: Long) = Spot(1, Address("", "", ""), "", "", "", price)