
package test;

import junit.framework.TestCase;

/**
 * Base Case
 */
public class BaseCase extends TestCase {
    /**
     * The Start at.
     */
    public long startAt = 0;

    /**
     * The End at.
     */
    public long endAt = 0;

    @Override
    public void setUp() throws Exception {
        this.startAt = System.currentTimeMillis();
    }

    @Override
    public void tearDown() throws Exception {
        this.endAt = System.currentTimeMillis();
        System.out.printf("run time: %6d ms\n\n", this.endAt - this.startAt);
    }

}
