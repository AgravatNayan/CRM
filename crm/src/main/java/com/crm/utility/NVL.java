package com.crm.utility;

import java.sql.Blob;

public class NVL {
	public static String StringNvl(String val)
    {
        if(val != null) {
            return val;
        }
        return "";
    }
	
	public static int intNvl(int val)
    {
        if(String.valueOf(val) != null) {
            return val;
        }
        return 0;
    }		
}
