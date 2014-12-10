package com.enderwolf.spotipower;

import junit.framework.TestSuite;

/**
 *
 * Created by vayner on 10.12.14.
 */
public class TestCollection {

    public TestCollection() {
        TestSuite suite = new TestSuite();
        suite.addTest(new ParserTest());

        suite.run(null);
    }
}
