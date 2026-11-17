package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GcdTest {

    @Test
    @DisplayName("G1: gcd(7,0) loop not entered. x13->20, y13->15 F")
    void gcd_loopNotEntered() {
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    @Test
    @DisplayName("G2: gcd(12,8) two iterations. x13->16, x17->16, x17->20, y13->15 T, y13->16, y13->17, y18->15 T/F, y18->16, y18->17, tmp16->18")
    void gcd_twoIterations() {
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }
}
