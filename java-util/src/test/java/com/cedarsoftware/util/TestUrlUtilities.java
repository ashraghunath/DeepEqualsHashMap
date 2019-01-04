package com.cedarsoftware.util;

import org.junit.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
public class TestUrlUtilities
{
    private static final String httpsUrl = "https://gotofail.com/";
    private static final String domain  = "ssllabs";
    private static final String httpUrl = "http://files.cedarsoftware.com/tests/ncube/some.txt";
    private static final String _expected = "CAFEBABE";

    @Test
    public void testConstructorIsPrivate() throws Exception
    {
        Class c = UrlUtilities.class;
        assertEquals(Modifier.FINAL, c.getModifiers() & Modifier.FINAL);

        Constructor<UrlUtilities> con = UrlUtilities.class.getDeclaredConstructor();
        assertEquals(Modifier.PRIVATE, con.getModifiers() & Modifier.PRIVATE);
        con.setAccessible(true);

        assertNotNull(con.newInstance());
    }

    @Test
    public void testGetContentFromUrlAsString() throws Exception
    {
        String content1 = UrlUtilities.getContentFromUrlAsString(httpsUrl, Proxy.NO_PROXY);
        String content2 = UrlUtilities.getContentFromUrlAsString(httpsUrl);
        String content3 = UrlUtilities.getContentFromUrlAsString(new URL(httpsUrl), true);
        String content4 = UrlUtilities.getContentFromUrlAsString(new URL(httpsUrl), null, null, true);
        String content5 = UrlUtilities.getContentFromUrlAsString(httpsUrl, null, 0, null, null, true);
        String content6 = UrlUtilities.getContentFromUrlAsString(httpsUrl, null, null, true);

        assertTrue(content1.contains(domain));
        assertTrue(content2.contains(domain));
        assertTrue(content3.contains(domain));
        assertTrue(content4.contains(domain));
        assertTrue(content5.contains(domain));
        assertTrue(content6.contains(domain));

        assertEquals(content1, content2);

        String content7 = UrlUtilities.getContentFromUrlAsString(httpUrl, Proxy.NO_PROXY);
        String content8 = UrlUtilities.getContentFromUrlAsString(httpUrl);
        String content9 = UrlUtilities.getContentFromUrlAsString(httpUrl, null, 0, null, null, true);
        String content10 = UrlUtilities.getContentFromUrlAsString(httpUrl, null, null, true);

        assertEquals(_expected, content7);
        assertEquals(_expected, content8);
        assertEquals(_expected, content9);
        assertEquals(_expected, content10);
    }

    @Test
    public void testNaiveTrustManager() throws Exception
    {
        TrustManager[] managers = UrlUtilities.NAIVE_TRUST_MANAGER;

        for (TrustManager tm : managers)
        {
            X509TrustManager x509Manager = (X509TrustManager)tm;
            try {
                x509Manager.checkClientTrusted(null, null);
                x509Manager.checkServerTrusted(null, null);
            } catch (Exception e) {
                fail();
            }
            assertNull(x509Manager.getAcceptedIssuers());
        }
    }


    @Test
    public void testNaiveVerifier() throws Exception
    {
        HostnameVerifier verifier = UrlUtilities.NAIVE_VERIFIER;
        assertTrue(verifier.verify(null, null));
    }

    @Test
    public void testReadErrorResponse() throws Exception {
        UrlUtilities.readErrorResponse(null);

        HttpURLConnection c1 = mock(HttpURLConnection.class);
        when(c1.getResponseCode()).thenThrow(new ConnectException());
        UrlUtilities.readErrorResponse(c1);

        verify(c1, times(1)).getResponseCode();

        HttpURLConnection c2 = mock(HttpURLConnection.class);
        when(c2.getResponseCode()).thenThrow(new IOException());
        UrlUtilities.readErrorResponse(c2);
        verify(c2, times(1)).getResponseCode();

        HttpURLConnection c3 = mock(HttpURLConnection.class);
        when(c3.getResponseCode()).thenThrow(new RuntimeException());
        UrlUtilities.readErrorResponse(c3);
        verify(c3, times(1)).getResponseCode();
    }

    @Test
    public void testComparePaths() {
        assertTrue(UrlUtilities.comparePaths(null, "anytext"));
        assertTrue(UrlUtilities.comparePaths("/", "anything"));
        assertTrue(UrlUtilities.comparePaths("/foo", "/foo/notfoo"));
        assertFalse(UrlUtilities.comparePaths("/foo/", "/bar/"));
    }

    @Test
    public void testIsNotExpired() {
        assertFalse(UrlUtilities.isNotExpired(""));
    }

    @Test
    public void testGetContentFromUrlWithMalformedUrl() {
        assertNull(UrlUtilities.getContentFromUrl("", null, null, true));
        assertNull(UrlUtilities.getContentFromUrl("", null, null, null, true));

        assertNull(UrlUtilities.getContentFromUrl("www.google.com", "localhost", 80, null, null, true));
    }

    @Test
    public void testGetContentFromUrl() throws Exception
    {
        SSLSocketFactory f = UrlUtilities.naiveSSLSocketFactory;
        HostnameVerifier v = UrlUtilities.NAIVE_VERIFIER;

        String content1 = new String(UrlUtilities.getContentFromUrl(httpsUrl, Proxy.NO_PROXY));
        String content2 = new String(UrlUtilities.getContentFromUrl(new URL(httpsUrl), null, null, true));
        String content3 = new String(UrlUtilities.getContentFromUrl(httpsUrl, Proxy.NO_PROXY, f, v));
        String content4 = new String(UrlUtilities.getContentFromUrl(httpsUrl, null, 0, null, null, true));
        String content5 = new String(UrlUtilities.getContentFromUrl(httpsUrl, null, null, true));
        String content6 = new String(UrlUtilities.getContentFromUrl(httpsUrl, null, null, Proxy.NO_PROXY, f, v));
        String content7 = new String(UrlUtilities.getContentFromUrl(new URL(httpsUrl), true));

        //  Allow for small difference between pages between requests to handle time and hash value changes.
        assertEquals(content1, content2);
        assertEquals(content2, content3);
        assertEquals(content3, content4);
        assertEquals(content4, content5);
        assertEquals(content5, content6);
        assertEquals(content6, content7);

        String content10 = new String(UrlUtilities.getContentFromUrl(httpUrl, Proxy.NO_PROXY, null, null));
        String content11 = new String(UrlUtilities.getContentFromUrl(httpUrl, null, null));
        String content12 = new String(UrlUtilities.getContentFromUrl(httpUrl, null, 0, null, null, false));
        String content13 = new String(UrlUtilities.getContentFromUrl(httpUrl, null, null, false));
        String content14 = new String(UrlUtilities.getContentFromUrl(httpUrl, null, null, Proxy.NO_PROXY, null, null));

        assertEquals(content10, content11);
        assertEquals(content11, content12);
        assertEquals(content12, content13);
        assertEquals(content13, content14);

        // 404
        assertNull(UrlUtilities.getContentFromUrl(httpUrl + "/google-bucks.html", null, null, Proxy.NO_PROXY, null, null));
    }

    @Test
    public void testSSLTrust() throws Exception
    {
        String content1 = UrlUtilities.getContentFromUrlAsString(httpsUrl, Proxy.NO_PROXY);
        String content2 = UrlUtilities.getContentFromUrlAsString(httpsUrl, null, 0, null, null, true);

        assertTrue(content1.contains(domain));
        assertTrue(content2.contains(domain));

        assertTrue(StringUtilities.levenshteinDistance(content1, content2) < 10);

    }

    @Test
    public void testCookies() throws Exception
    {
        Map cookies = new HashMap();

        byte[] bytes1 = UrlUtilities.getContentFromUrl(httpUrl, null, 0, cookies, cookies, false);

        assertEquals(1, cookies.size());
        assertTrue(cookies.containsKey("cedarsoftware.com"));
        assertEquals(_expected, new String(bytes1));
    }

    @Test
    public void testHostName()
    {
        assertNotNull(UrlUtilities.getHostName());
    }

    @Test
    public void testGetConnection() throws Exception
    {
        URL u = TestIOUtilities.class.getClassLoader().getResource("io-test.txt");
        compareIO(UrlUtilities.getConnection(u, true, false, false));
        compareIO(UrlUtilities.getConnection(u, null, 0, null, true, false, false, true));
        compareIO(UrlUtilities.getConnection(u, null, true, false, false, true));
        compareIO(UrlUtilities.getConnection(u, null, true, false, false, Proxy.NO_PROXY, true));
    }

    private void compareIO(URLConnection c) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
        InputStream s = c.getInputStream();
        IOUtilities.transfer(s, out);
        IOUtilities.close(s);

        assertArrayEquals("This is for an IO test!".getBytes(), out.toByteArray());
    }

    @Test
    public void testGetConnection1() throws Exception
    {
        HttpURLConnection c = (HttpURLConnection) UrlUtilities.getConnection("http://www.yahoo.com", true, false, false);
        assertNotNull(c);
        c.connect();
        UrlUtilities.disconnect(c);
    }

//    @Test
//    public void testGetConnection2() throws Exception
//    {
//        HttpURLConnection c = (HttpURLConnection) UrlUtilities.getConnection(new URL("http://www.yahoo.com"), true, false, false);
//        assertNotNull(c);
//        UrlUtilities.setTimeouts(c, 9000, 10000);
//        c.connect();
//        UrlUtilities.disconnect(c);
//    }

    @Test
    public void testCookies2() throws Exception
    {
        Map cookies = new HashMap();
        Map gCookie = new HashMap();
        gCookie.put("param", new HashMap());
        cookies.put("google.com", gCookie);
        HttpURLConnection c = (HttpURLConnection) UrlUtilities.getConnection(new URL("http://www.google.com"), cookies, true, false, false, null, null, null);
        UrlUtilities.setCookies(c, cookies);
        c.connect();
        Map outCookies = new HashMap();
        UrlUtilities.getCookies(c, outCookies);
        UrlUtilities.disconnect(c);
    }

    @Test
    public void testUserAgent() throws Exception
    {
        UrlUtilities.clearGlobalUserAgent();
        UrlUtilities.setUserAgent(null);
        assertNull(UrlUtilities.getUserAgent());

        UrlUtilities.setUserAgent("global");
        assertEquals("global", UrlUtilities.getUserAgent());

        UrlUtilities.setUserAgent("local");
        assertEquals("local", UrlUtilities.getUserAgent());

        UrlUtilities.setUserAgent(null);
        assertEquals("global", UrlUtilities.getUserAgent());

        UrlUtilities.clearGlobalUserAgent();
        assertEquals(null, UrlUtilities.getUserAgent());
    }

    @Test
    public void testReferrer() throws Exception
    {
        UrlUtilities.clearGlobalReferrer();
        UrlUtilities.setReferrer(null);
        assertNull(UrlUtilities.getReferrer());

        UrlUtilities.setReferrer("global");
        assertEquals("global", UrlUtilities.getReferrer());

        UrlUtilities.setReferrer("local");
        assertEquals("local", UrlUtilities.getReferrer());

        UrlUtilities.setReferrer(null);
        assertEquals("global", UrlUtilities.getReferrer());

        UrlUtilities.clearGlobalReferrer();
        assertEquals(null, UrlUtilities.getReferrer());
    }
}
