package com.ubtech.airport.ibm;

import com.ubtech.airport.ibm.airport.CAAApi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CaaTest {
    @Test
    public void testCaaLogin(){
        assertEquals(CAAApi.login("18616350016"),true);
    }
}
