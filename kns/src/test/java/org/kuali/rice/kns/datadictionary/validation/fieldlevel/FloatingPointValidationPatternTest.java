/*
 * Copyright 2005-2007 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kns.datadictionary.validation.fieldlevel;

import org.junit.Test;
import org.kuali.test.KNSTestCase;
import org.kuali.test.KNSWithTestSpringContext;


@KNSWithTestSpringContext
public class FloatingPointValidationPatternTest extends KNSTestCase {
    FloatingPointValidationPattern pattern;

    @Override
    public final void setUp() throws Exception {
        super.setUp();

        pattern = new FloatingPointValidationPattern();
    }


    @Test public final void testDefaultAllows_positive1() {
        assertTrue(pattern.matches(".1"));
    }

    @Test public final void testDefaultAllows_positive2() {
        assertTrue(pattern.matches("0.1"));
    }

    @Test public final void testDefaultAllows_positive3() {
        assertTrue(pattern.matches("1.1"));
    }

    @Test public final void testDefaultAllows_positive4() {
        assertTrue(pattern.matches("1"));
    }

    @Test public final void testDefaultAllows_positive5() {
        assertTrue(pattern.matches("1.0"));
    }


    @Test public final void testDefaultAllows_negative1() {
        assertFalse(pattern.matches("-.1"));
    }

    @Test public final void testDefaultAllows_negative2() {
        assertFalse(pattern.matches("-0.1"));
    }

    @Test public final void testDefaultAllows_negative3() {
        assertFalse(pattern.matches("-1.1"));
    }

    @Test public final void testDefaultAllows_negative4() {
        assertFalse(pattern.matches("-1"));
    }

    @Test public final void testDefaultAllows_negative5() {
        assertFalse(pattern.matches("-1.0"));
    }


    @Test public final void testDefaultAllows_invalid1() {
        assertFalse(pattern.matches("-."));
    }

    @Test public final void testDefaultAllows_invalid2() {
        assertFalse(pattern.matches("1."));
    }

    @Test public final void testDefaultAllows_invalid3() {
        assertFalse(pattern.matches("-1."));
    }


    @Test public final void testAllowNegative_positive1() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches(".1"));
    }

    @Test public final void testAllowNegative_positive2() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("0.1"));
    }

    @Test public final void testAllowNegative_positive3() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("1.1"));
    }

    @Test public final void testAllowNegative_positive4() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("1"));
    }

    @Test public final void testAllowNegative_positive5() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("1.0"));
    }


    @Test public final void testAllowNegative_negative1() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-.1"));
    }

    @Test public final void testAllowNegative_negative2() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-0.1"));
    }

    @Test public final void testAllowNegative_negative3() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-1.1"));
    }

    @Test public final void testAllowNegative_negative4() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-1"));
    }

    @Test public final void testAllowNegative_negative5() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-1.0"));
    }


    @Test public final void testAllowNegative_invalid1() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("-."));
    }

    @Test public final void testAllowNegative_invalid2() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("1."));
    }

    @Test public final void testAllowNegative_invalid3() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("-1."));
    }
}