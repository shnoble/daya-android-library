package com.daya.android.util;

/**
 * Created by shhong on 2017. 9. 25..
 */
public class JsonUtilTest {
//    private JSONObject mJsonObject;
//    private JSONArray mJsonArray;
//
//    @Before
//    public void setUpJsonObject() throws Exception {
//        Map<String, Object> data = new HashMap<>();
//        data.put("a", "1");
//        data.put("b", "2");
//        data.put("c", "3");
//        mJsonObject = new JSONObject(data);
//
//        Map<String, Object> subData1 = new HashMap<>();
//        subData1.put("sub_data_a", "sub_data_1");
//        subData1.put("sub_data_b", "sub_data_2");
//        mJsonObject.put("subData1", new JSONObject(subData1));
//
//        Map<String, Object> subData2 = new HashMap<>();
//        subData2.put("sub_int_a", 1);
//        subData2.put("sub_int_b", 2);
//        mJsonObject.put("subData2", new JSONObject(subData2));
//    }
//
//    @Before
//    public void setUpJsonArray() throws Exception {
//        List<Object> data = new LinkedList<>();
//        data.add("1");
//        data.add("2");
//        data.add("3");
//        mJsonArray = new JSONArray(data);
//
//        List<Object> subData1 = new LinkedList<>();
//        subData1.add("sub_1");
//        subData1.add("sub_2");
//        subData1.add("sub_3");
//        mJsonArray.put(new JSONArray(subData1));
//
//        List<Object> subData2 = new LinkedList<>();
//        subData2.add(1);
//        subData2.add(2);
//        subData2.add(3);
//        mJsonArray.put(new JSONArray(subData2));
//    }
//
//    @Test
//    public void testJSONObjectToMap() throws Exception {
//        Map<String, Object> result = JsonHelper.JSONObjectToMap(mJsonObject);
//        assertNotNull(result);
//
//        assertEquals("1", result.get("a"));
//        assertEquals("2", result.get("b"));
//        assertEquals("3", result.get("c"));
//
//
//        Map<String, Object> subData1 = (Map<String, Object>) result.get("subData1");
//        assertNotNull(subData1);
//
//        assertEquals("sub_data_1", subData1.get("sub_data_a"));
//        assertEquals("sub_data_2", subData1.get("sub_data_b"));
//
//        Map<String, Object> subData2 = (Map<String, Object>) result.get("subData2");
//        assertNotNull(subData2);
//
//        assertEquals(1, subData2.get("sub_int_a"));
//        assertEquals(2, subData2.get("sub_int_b"));
//    }
//
//    @Test
//    public void testJSONArrayToList() throws Exception {
//        List<Object> result = JsonHelper.JSONArrayToList(mJsonArray);
//        assertNotNull(result);
//
//        assertEquals("1", result.get(0));
//        assertEquals("2", result.get(1));
//        assertEquals("3", result.get(2));
//
//        List<Object> subData1 = (List<Object>) result.get(3);
//        assertNotNull(subData1);
//
//        assertEquals("sub_1", subData1.get(0));
//        assertEquals("sub_2", subData1.get(1));
//        assertEquals("sub_3", subData1.get(2));
//
//        List<Object> subData2 = (List<Object>) result.get(4);
//        assertNotNull(subData2);
//
//        assertEquals(1, subData2.get(0));
//        assertEquals(2, subData2.get(1));
//        assertEquals(3, subData2.get(2));
//    }
}