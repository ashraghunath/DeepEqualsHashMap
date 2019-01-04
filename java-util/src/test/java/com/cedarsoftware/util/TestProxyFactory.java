package com.cedarsoftware.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ken Partlow
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
public class TestProxyFactory
{
    @Test
    public void testClassCompliance() throws Exception {
        Class c = ProxyFactory.class;
        Assert.assertEquals(Modifier.FINAL, c.getModifiers() & Modifier.FINAL);

        Constructor<ProxyFactory> con = c.getDeclaredConstructor();
        Assert.assertEquals(Modifier.PRIVATE, con.getModifiers() & Modifier.PRIVATE);

        con.setAccessible(true);
        Assert.assertNotNull(con.newInstance());
    }

    @Test
    public void testProxyFactory() {
        final Set<String> set = new HashSet<String>();

        AInt i = ProxyFactory.create(AInt.class, new InvocationHandler(){

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                set.add(method.getName());
                return null;
            }
        });

        assertTrue(set.isEmpty());
        i.foo();
        assertTrue(set.contains("foo"));
        assertFalse(set.contains("bar"));
        assertFalse(set.contains("baz"));
        i.bar();
        assertTrue(set.contains("foo"));
        assertTrue(set.contains("bar"));
        assertFalse(set.contains("baz"));
        i.baz();
        assertTrue(set.contains("foo"));
        assertTrue(set.contains("bar"));
        assertTrue(set.contains("baz"));
    }

    private interface AInt
    {
        public void foo();
        public void bar();
        public void baz();
    }


}
