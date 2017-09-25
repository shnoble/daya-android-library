package com.daya.android.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 25..
 */
public class JsonTest {
    @Test
    public void testConstructorWithMap() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("a", "1");
        data.put("b", "2");
        data.put("c", "3");

        Json json = new Json(data);
        Map<String, Object> result = json.toMap();
        assertNotNull(result);
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
        assertEquals("3", result.get("c"));
    }

    @Test
    public void testConstructorWithList() throws Exception {
        List<Object> data = new LinkedList<>();
        data.add("1");
        data.add("2");
        data.add("3");

        Json json = new Json(data);
        System.out.println(json);

        List<Object> result = json.toList();
        assertNotNull(result);
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }
}