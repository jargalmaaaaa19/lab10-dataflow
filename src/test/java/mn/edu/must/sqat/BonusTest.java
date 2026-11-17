package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BonusTest {

    // ===== STAGE 1: ALL-DEFS =====
    @Test
    @DisplayName("allDefs A: 50=T, 53=T. score51->54, score54->56, streak46->41")
    void allDefs_longStreakThenBreak() {
        assertEquals(8, DataFlowTargets.bonus(new int[]{1,1,1,1,1,0,1}, 3));
    }

    @Test
    @DisplayName("allDefs B: empty array, 50=F, 53=F. score49->56, best37->53")
    void allDefs_emptyBelowThreshold() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{}, 1));
    }

    // ===== STAGE 2: ALL-DU-PAIRS =====
    @Test @DisplayName("P1 T: attendance==null")
    void nullAttendanceThrows() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(null, 0));
    }

    @Test @DisplayName("P3 T: threshold<0 (attendance non-null so || reaches it)")
    void negativeThresholdThrows() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(new int[]{1}, -1));
    }

    @Test @DisplayName("P7 T: present35->50 true (threshold=0), score51->56")
    void emptyArrayThresholdZero() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{}, 0));
    }

    @Test @DisplayName("P9 T, P10, P12 T, P13, P15 T, P18 F")
    void singleDayMeetsThreshold() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{1}, 1));
    }

    @Test @DisplayName("P9 F: present40->50 false")
    void singleDayBelowThreshold() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{1}, 2));
    }

    @Test @DisplayName("P12 F, P17 F, P14: streak does not beat best")
    void streakDoesNotBeatBest() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{1,0,1}, 1));
    }

    @Test @DisplayName("P8, P11, P17 T: consecutive presents")
    void streakGrowsBeyondBest() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{1,1}, 2));
    }

    @Test @DisplayName("P19: score49->54 (50=F, 53=T)")
    void longStreakBelowThreshold() {
        assertEquals(3, DataFlowTargets.bonus(new int[]{1,1,1,1,1}, 6));
    }

    @Test @DisplayName("Boundary: exactly threshold and exactly 5")
    void exactFiveExactThreshold() {
        assertEquals(8, DataFlowTargets.bonus(new int[]{1,1,1,1,1}, 5));
    }
}
