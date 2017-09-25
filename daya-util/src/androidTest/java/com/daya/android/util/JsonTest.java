package com.daya.android.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 25..
 */
public class JsonTest {
    private String mJsonString;
    private Map<String, Object> mRootMap, mSubMap1, mSubMap2;
    private List<Object> mRootList, mSubList1, mSubList2;

    @Before
    public void setUpJsonString() throws Exception {
        mJsonString = "{\n" +
                "  \"a\": \"1\",\n" +
                "  \"b\": \"2\",\n" +
                "  \"c\": \"3\",\n" +
                "  \"d\": {\n" +
                "    \"sub_a\": \"sub_1\",\n" +
                "    \"sub_b\": \"sub_2\"\n" +
                "  },\n" +
                "  \"e\": {\n" +
                "    \"sub_a\": 1,\n" +
                "    \"sub_b\": 2\n" +
                "  }\n" +
                "}";
    }

    @Before
    public void setUpMap() throws Exception {
        mRootMap = new HashMap<>();
        mRootMap.put("a", "1");
        mRootMap.put("b", "2");
        mRootMap.put("c", "3");

        mSubMap1 = new HashMap<>();
        mSubMap1.put("sub_a", "sub_1");
        mSubMap1.put("sub_b", "sub_2");

        mSubMap2 = new HashMap<>();
        mSubMap2.put("sub_a", 1);
        mSubMap2.put("sub_b", 2);
    }

    @Before
    public void setUpList() throws Exception {
        mRootList = new ArrayList<>();
        mRootList.add("1");
        mRootList.add("2");
        mRootList.add("3");

        mSubList1 = new ArrayList<>();
        mSubList1.add("sub_1");
        mSubList1.add("sub_2");
        mSubList1.add("sub_3");

        mSubList2 = new ArrayList<>();
        mSubList2.add(1);
        mSubList2.add(2);
        mSubList2.add(3);
    }

    @Test
    public void testConstructor() throws Exception {
        Json json = new Json();
        assertNotNull(json);
    }

    @Test
    public void testConstructorWithMap() throws Exception {
        mRootMap.put("d", mSubMap1);
        mRootMap.put("e", mSubMap2);

        Json json = new Json(mRootMap);
        assertNotNull(json);

        System.out.println(json);

        Map<String, Object> root = json.toMap();
        assertNotNull(root);
        assertEquals("1", root.get("a"));
        assertEquals("2", root.get("b"));
        assertEquals("3", root.get("c"));

        Map sub1 = (Map) root.get("d");
        assertNotNull(sub1);
        assertEquals("sub_1", sub1.get("sub_a"));
        assertEquals("sub_2", sub1.get("sub_b"));

        Map sub2 = (Map) root.get("e");
        assertNotNull(sub2);
        assertEquals(1, sub2.get("sub_a"));
        assertEquals(2, sub2.get("sub_b"));
    }

    @Test
    public void testConstructorWithList() throws Exception {
        mRootList.add(mSubList1);
        mRootList.add(mSubList2);

        Json json = new Json(mRootList);
        assertNotNull(json);

        System.out.println(json);

        List root = json.toList();
        assertNotNull(root);
        assertEquals("1", root.get(0));
        assertEquals("2", root.get(1));
        assertEquals("3", root.get(2));

        List sub1 = (List) root.get(3);
        assertNotNull(sub1);
        assertEquals("sub_1", sub1.get(0));
        assertEquals("sub_2", sub1.get(1));
        assertEquals("sub_3", sub1.get(2));

        List sub2 = (List) root.get(4);
        assertNotNull(sub2);
        assertEquals(1, sub2.get(0));
        assertEquals(2, sub2.get(1));
        assertEquals(3, sub2.get(2));
    }

    @Test
    public void testConstructorWithString() throws Exception {
        Json json = new Json(mJsonString);
        assertNotNull(json);

        System.out.println(json);

        Map<String, Object> root = json.toMap();
        assertNotNull(root);
        assertEquals("1", root.get("a"));
        assertEquals("2", root.get("b"));
        assertEquals("3", root.get("c"));

        Map sub1 = (Map) root.get("d");
        assertNotNull(sub1);
        assertEquals("sub_1", sub1.get("sub_a"));
        assertEquals("sub_2", sub1.get("sub_b"));

        Map sub2 = (Map) root.get("e");
        assertNotNull(sub2);
        assertEquals(1, sub2.get("sub_a"));
        assertEquals(2, sub2.get("sub_b"));
    }
}

