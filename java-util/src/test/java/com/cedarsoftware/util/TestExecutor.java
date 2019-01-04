package com.cedarsoftware.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author John DeRegnaucourt (john@cedarsoftware.com)
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
public class TestExecutor
{
    private static final String THIS_IS_HANDY = "This is handy";
    private static final String ECHO_THIS_IS_HANDY = "echo " + THIS_IS_HANDY;

    @Test
    public void testExecutor()
    {
        Executor executor = new Executor();

        String s = System.getProperty("os.name");

        if (s.toLowerCase().contains("windows")) {
            executor.exec(new String[] {"cmd.exe", "/c", ECHO_THIS_IS_HANDY});
        } else {
            executor.exec(ECHO_THIS_IS_HANDY);
        }
        assertEquals(THIS_IS_HANDY, executor.getOut().trim());
    }
}
