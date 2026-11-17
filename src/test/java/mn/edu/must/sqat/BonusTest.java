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
}
