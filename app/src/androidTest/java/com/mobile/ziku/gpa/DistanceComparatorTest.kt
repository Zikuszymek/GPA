package com.mobile.ziku.gpa

import android.location.Location
import android.support.test.runner.AndroidJUnit4
import com.mobile.ziku.gpa.managers.DistanceComparator
import com.mobile.ziku.gpa.model.Geometry
import com.mobile.ziku.gpa.model.PlaceSearched
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DistanceComparatorTest {

    lateinit var searchedList: List<PlaceSearched>

    companion object {
        const val MY_LOCATION = "Rondo"
        const val SPODEK = "Spodek"
        const val PKO_BANK = "BankPKO"
        const val BUS_STATION = "DworzecAutobusowy"
        const val MARKET_PLACE = "Rynek"
        const val THEATER = "Theater"
    }

    @Before
    fun initializeList() {
        searchedList = listOf(
                PlaceSearched(geometry = Geometry(busStation), name = BUS_STATION),
                PlaceSearched(geometry = Geometry(theater), name = THEATER),
                PlaceSearched(geometry = Geometry(bankPKO), name = PKO_BANK),
                PlaceSearched(geometry = Geometry(marketplace), name = MARKET_PLACE),
                PlaceSearched(geometry = Geometry(Spodek), name = SPODEK)
        )
    }

    @Test
    fun checkOrderBeforeSorted() {
        assertEquals(BUS_STATION, searchedList[0].name)
        assertEquals(THEATER, searchedList[1].name)
        assertEquals(PKO_BANK, searchedList[2].name)
        assertEquals(MARKET_PLACE, searchedList[3].name)
        assertEquals(SPODEK, searchedList[4].name)
    }

    @Test
    fun checkOrderAfterSorted() {
        val sortedList = searchedList.sortedWith(DistanceComparator(myLocation))
        assertEquals(PKO_BANK, sortedList[0].name)
        assertEquals(SPODEK, sortedList[1].name)
        assertEquals(MARKET_PLACE, sortedList[2].name)
        assertEquals(BUS_STATION, sortedList[3].name)
        assertEquals(THEATER, sortedList[4].name)
    }

    private fun getLocation(lat: Double, lng: Double, name: String): Location {
        return Location(name).apply {
            latitude = lat
            longitude = lng
        }
    }

    val myLocation: Location by lazy { getLocation(50.264305, 19.02353, MY_LOCATION) }
    val Spodek: Location by lazy { getLocation(50.265827, 19.025161, SPODEK) }
    val bankPKO: Location by lazy { getLocation(50.265379, 19.022468, PKO_BANK) }
    val busStation: Location by lazy { getLocation(50.262722, 19.017051, BUS_STATION) }
    val marketplace: Location by lazy { getLocation(50.260077, 19.022221, MARKET_PLACE) }
    val theater: Location by lazy { getLocation(50.259671, 19.023146, THEATER) }

}