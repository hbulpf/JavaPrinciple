package dev.wjw.demo.javaSE.interfaceCourse.interfaceDemo1;

import org.junit.Test;

/**
 * @author wjw
 * @version 1.0
 * @date 2020/2/3 23:05
 */

//
public class TestLockDoor {

    @Test
    public void test_LockDoor() {
        LockDoor lockDoor = new LockDoor();

        lockDoor.openDoor();
        lockDoor.closeDoor();

        lockDoor.openLock();
        lockDoor.closeLock();
    }

}
