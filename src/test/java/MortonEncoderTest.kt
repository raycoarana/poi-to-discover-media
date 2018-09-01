import com.raycoarana.poitodiscover.core.MortonEncoder
import org.junit.Assert.assertEquals
import org.junit.Test

class MortonEncoderTest {

    private val encoder = MortonEncoder()

    @Test
    fun tesMorton() {

        compare(0L, 0.0, 0.0)
        compare(96076792050570240L, 0.0, 45.0)
        compare(384307168202280960L, 0.0, 90.0)
        compare(5860684315084784640L, 0.0, -45.0)
        compare(4995993186629652480L, 0.0, -90.0)
        compare(768614336404561920L, 90.0, 0.0)
        compare(6734162532828480880L, 51.5309982299805, -2.53273010253906)
        compare(6733409682878134185L, 51.71995, -3.45621)
        compare(583848065977581386L, 49.9161371, 6.1797018)
        compare(3321195214889472278L, -21.063410, 55.708730)
    }

    private fun compare(coded: Long, lat: Double, lng: Double) {
        val expectedPair = encoder.decode(coded)
        val actualPair = encoder.decode(encoder.encode(lat, lng))

        assertEquals("Latitude", expectedPair.first, actualPair.first, 1e-4)
        assertEquals("Longitude", expectedPair.second, actualPair.second, 1e-4)
    }
}
