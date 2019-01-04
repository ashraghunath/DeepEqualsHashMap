package com.cedarsoftware.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(PowerMockRunner.class)
public class TestTrackingMap
{
    @Mock
    public Map<String, Object> mockedBackingMap;

    @Mock
    public Map<String, Object> anotherMockedBackingMap;


    @Test
    public void getFree() {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "value");
        map.put("second", "value");
        map.expungeUnused();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void getOne() {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "value");
        map.get("first");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("first"), "firstValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void getOneCaseInsensitive() {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "value");
        map.get("FiRsT");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("first"), "firstValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void getOneMultiple() {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "value");
        map.get("FiRsT");
        map.get("FIRST");
        map.get("First");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("first"), "firstValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void containsKeyCounts() {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "value");
        map.containsKey("first");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("first"), "firstValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void containsValueDoesNotCount() {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "value");
        map.containsValue("firstValue");
        map.expungeUnused();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void sameBackingMapsAreEqual() {
        CaseInsensitiveMap<String, Object> backingMap = new CaseInsensitiveMap<>();
        TrackingMap<String, Object> map1 = new TrackingMap<>(backingMap);
        TrackingMap<String, Object> map2 = new TrackingMap<>(backingMap);
        assertEquals(map1, map2);
    }

    @Test
    public void equalBackingMapsAreEqual() {
        Map map1 = new TrackingMap<>(new HashMap<>());
        Map map2 = new TrackingMap<>(new HashMap<>());
        assertEquals(map1, map2);

        map1.put('a', 65);
        map1.put('b', 66);
        map2 = new TrackingMap<>(new HashMap<>());
        map2.put('a', 65);
        map2.put('b', 66);
        assertEquals(map1, map2);
    }

    @Test
    public void unequalBackingMapsAreNotEqual()
    {
        Map map1 = new TrackingMap<>(new HashMap<>());
        Map map2 = new TrackingMap<>(new HashMap<>());
        assertEquals(map1, map2);

        map1.put('a', 65);
        map1.put('b', 66);
        map2 = new TrackingMap<>(new HashMap<>());
        map2.put('a', 65);
        map2.put('b', 66);
        map2.put('c', 67);
        assertNotEquals(map1, map2);
    }

    @Test
    public void testDifferentClassIsEqual()
    {
        CaseInsensitiveMap<String, Object> backingMap = new CaseInsensitiveMap<>();
        backingMap.put("a", "alpha");
        backingMap.put("b", "bravo");

        // Identity check
        Map map1 = new TrackingMap<>(backingMap);
        assert map1.equals(backingMap);

        // Equivalence check
        Map map2 = new LinkedHashMap<>();
        map2.put("b", "bravo");
        map2.put("a", "alpha");

        assert map1.equals(map2);
    }

    @Test
    public void testGet() throws Exception {
        Map<String, Object> map = new TrackingMap<>(mockedBackingMap);
        map.get("key");
        verify(mockedBackingMap).get("key");
    }

    @Test
    public void testPut() throws Exception {
        Map<String, Object> map = new TrackingMap<>(mockedBackingMap);
        map.put("key", "value");
        verify(mockedBackingMap).put("key", "value");
    }

    @Test
    public void testContainsKey() throws Exception {
        Map<String, Object> map = new TrackingMap<>(mockedBackingMap);
        map.containsKey("key");
        verify(mockedBackingMap).containsKey("key");
    }

    @Test
    public void testPutAll() throws Exception {
        Map<String, Object> map = new TrackingMap<>(mockedBackingMap);
        Map additionalEntries = new HashMap();
        additionalEntries.put("animal", "aardvaark");
        additionalEntries.put("ballast", "bubbles");
        additionalEntries.put("tricky", additionalEntries);
        map.putAll(additionalEntries);
        verify(mockedBackingMap).putAll(additionalEntries);
    }

    @Test
    public void testRemove() throws Exception {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        map.get("FiRsT");
        map.get("ThirD");
        map.remove("first");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("thiRd"), "thirdValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void testHashCode() throws Exception {
        Map<String, Object> map1 = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map1.put("f", "foxtrot");
        map1.put("o", "oscar");

        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("o", "foxtrot");
        map2.put("f", "oscar");

        Map map3 = new TrackingMap<>(new CaseInsensitiveMap<>());
        map3.put("F", "foxtrot");
        map3.put("O", "oscar");

        assert map1.hashCode() == map2.hashCode();
        assert map2.hashCode() == map3.hashCode();
    }

    @Test
    public void testToString() throws Exception {
        TrackingMap<String, Object> map = new TrackingMap<>(mockedBackingMap);
        assertNotNull(map.toString());
    }

    @Test
    public void testClear() throws Exception {
        Map<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        map.get("FiRsT");
        map.get("ThirD");
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testValues() throws Exception {
        Map<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        Collection<Object> values = map.values();
        assertNotNull(values);
        assertEquals(3, map.size());
        assertTrue(values.contains("firstValue"));
        assertTrue(values.contains("secondValue"));
        assertTrue(values.contains("thirdValue"));
    }

    @Test
    public void testKeySet() throws Exception {
        Map<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        Collection<String> keys = map.keySet();
        assertNotNull(keys);
        assertEquals(3, map.size());
        assertTrue(keys.contains("first"));
        assertTrue(keys.contains("second"));
        assertTrue(keys.contains("third"));
    }

    @Test
    public void testEntrySet() throws Exception {
        CaseInsensitiveMap<String, Object> backingMap = new CaseInsensitiveMap<>();
        Map<String, Object> map = new TrackingMap<>(backingMap);
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        Set<Map.Entry<String, Object>> keys = map.entrySet();
        assertNotNull(keys);
        assertEquals(3, keys.size());
        assertEquals(backingMap.entrySet(), map.entrySet());
    }

    @Test
    public void testInformAdditionalUsage() throws Exception {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        Collection<String> additionalUsage = new HashSet<>();
        additionalUsage.add("FiRsT");
        additionalUsage.add("ThirD");
        map.informAdditionalUsage(additionalUsage);
        map.remove("first");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("thiRd"), "thirdValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void testInformAdditionalUsage1() throws Exception {
        TrackingMap<String, Object> map = new TrackingMap<>(new CaseInsensitiveMap<String, Object>());
        map.put("first", "firstValue");
        map.put("second", "secondValue");
        map.put("third", "thirdValue");
        TrackingMap<String, Object> additionalUsage = new TrackingMap<>(map);
        additionalUsage.get("FiRsT");
        additionalUsage.get("ThirD");
        map.informAdditionalUsage(additionalUsage);
        map.remove("first");
        map.expungeUnused();
        assertEquals(1, map.size());
        assertEquals(map.get("thiRd"), "thirdValue");
        assertFalse(map.isEmpty());
    }

    @Test
    public void testConstructWithNull()
    {
        try
        {
            new TrackingMap(null);
            fail();
        }
        catch (IllegalArgumentException ignored)
        { }
    }

    @Test
    public void testPuDoesNotCountAsAccess()
    {
        TrackingMap trackMap = new TrackingMap(new CaseInsensitiveMap());
        trackMap.put("k", "kite");
        trackMap.put("u", "uniform");

        assert trackMap.keysUsed().isEmpty();

        trackMap.put("K", "kilo");
        assert trackMap.keysUsed().isEmpty();
        assert trackMap.size() == 2;
    }

    @Test
    public void testContainsKeyNotCoundOnNonExistentKey()
    {
        TrackingMap trackMap = new TrackingMap(new CaseInsensitiveMap());
        trackMap.put("y", "yankee");
        trackMap.put("z", "zulu");

        trackMap.containsKey("f");

        assert trackMap.keysUsed().size() == 1;
        assert trackMap.keysUsed().contains("f");
    }

    @Test
    public void testGetNotCoundOnNonExistentKey()
    {
        TrackingMap trackMap = new TrackingMap(new CaseInsensitiveMap());
        trackMap.put("y", "yankee");
        trackMap.put("z", "zulu");

        trackMap.get("f");

        assert trackMap.keysUsed().size() == 1;
        assert trackMap.keysUsed().contains("f");
    }

    @Test
    public void testGetOfNullValueCountsAsAccess()
    {
        TrackingMap trackMap = new TrackingMap(new CaseInsensitiveMap());

        trackMap.put("y", null);
        trackMap.put("z", "zulu");

        trackMap.get("y");

        assert trackMap.keysUsed().size() == 1;
    }

    @Test
    public void testFetchInternalMap()
    {
        TrackingMap trackMap = new TrackingMap(new CaseInsensitiveMap());
        assert trackMap.getWrappedMap() instanceof CaseInsensitiveMap;
        trackMap = new TrackingMap(new HashMap());
        assert trackMap.getWrappedMap() instanceof HashMap;
    }
}