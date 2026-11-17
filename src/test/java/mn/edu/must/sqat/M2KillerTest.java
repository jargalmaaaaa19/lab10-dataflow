package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Separate class so that M1-M3 can be run without it (-Dtest='!M2KillerTest'). */
class M2KillerTest {

    @Test
    @DisplayName("Kills M2: present=5 but longest streak=2")
    void presentFiveButStreakShort() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{1, 1, 0, 1, 1, 0, 1}, 0));
    }
}
