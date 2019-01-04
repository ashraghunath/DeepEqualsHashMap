package com.cedarsoftware.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * useful Array utilities
 *
 * @author Keneth Partlow
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public class TestArrayUtilities
{
    @Test
    public void testConstructorIsPrivate() throws Exception {
        Class c = ArrayUtilities.class;
        assertEquals(Modifier.FINAL, c.getModifiers() & Modifier.FINAL);

        Constructor<ArrayUtilities> con = c.getDeclaredConstructor();
        assertEquals(Modifier.PRIVATE, con.getModifiers() & Modifier.PRIVATE);
        con.setAccessible(true);

        assertNotNull(con.newInstance());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(ArrayUtilities.isEmpty(new byte[]{}));
        assertTrue(ArrayUtilities.isEmpty(null));
        assertFalse(ArrayUtilities.isEmpty(new byte[]{5}));
    }

    @Test
    public void testSize() {
        assertEquals(0, ArrayUtilities.size(new byte[]{}));
        assertEquals(0, ArrayUtilities.size(null));
        assertEquals(1, ArrayUtilities.size(new byte[]{5}));
    }

    @Test
    public void testShallowCopy() {
        String[] strings = new String[] { "foo", "bar", "baz"};
        String[] copy = (String[]) ArrayUtilities.shallowCopy(strings);
        assertNotSame(strings, copy);
        int i=0;
        for (String s: strings)
        {
            assertSame(s, copy[i++]);
        }

        assertNull(ArrayUtilities.shallowCopy(null));
    }

    @Test
    public void testAddAll() {
        assertEquals(0, ArrayUtilities.size(new byte[]{}));

        //  Test One
        Long[] one = new Long[] { 1L, 2L };
        Object[] resultOne = ArrayUtilities.addAll(null, one);
        assertNotSame(one, resultOne);
        for (int i=0; i<one.length; i++)
        {
            assertSame(one[i], resultOne[i]);
        }

        //  Test Two
        Long[] two = new Long[] { 3L, 4L };
        Object[] resultTwo = ArrayUtilities.addAll(two, null);
        assertNotSame(two, resultTwo);
        for (int i=0; i<two.length; i++) {
            assertSame(two[i], resultTwo[i]);
        }

        // Test Three
        Object[] resultThree = ArrayUtilities.addAll(one, two);
        assertNotSame(one, resultThree);
        assertNotSame(two, resultThree);
        for (int i=0; i<one.length; i++) {
            assertSame(one[i], resultThree[i]);
        }
        for (int i=0; i<two.length; i++) {
            assertSame(two[i], resultThree[i + one.length]);
        }
    }

    @Test(expected=ArrayStoreException.class)
    public void testInvalidClassDuringAddAll()
    {
        Long[] one = new Long[] { 1L, 2L };
        String[] two = new String[] {"foo", "bar"};
        ArrayUtilities.addAll(one, two);
    }

    @Test
    public void testRemoveItem()
    {
        String[] strings = new String[] { "foo", "bar", "baz"};
        assertEquals(3, strings.length);

        String[] test1 = ArrayUtilities.removeItem(strings, 2);
        String[] subsetTest1 = ArrayUtilities.getArraySubset(strings, 0, 2);
        String[] expected1 = new String[] { "foo", "bar" };

        assertArrayEquals(expected1, test1);
        assertArrayEquals(expected1, subsetTest1);

        String[] test2 = ArrayUtilities.removeItem(strings, 0);
        String[] subsetTest2 = ArrayUtilities.getArraySubset(strings, 1, 3);
        String[] expected2 = new String[] { "bar", "baz" };
        assertArrayEquals(expected2, test2);
        assertArrayEquals(expected2, subsetTest2);

        String[] test3 = ArrayUtilities.removeItem(strings, 1);
        String[] expected3 = new String[] { "foo", "baz" };

        assertArrayEquals(expected3, test3);
    }
}
