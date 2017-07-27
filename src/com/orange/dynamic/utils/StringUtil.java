package com.orange.dynamic.utils;

public class StringUtil {
	
	public static boolean isNULL(String arg){
		if(arg == null)
			return true;
		return false;
	}
	
	public static boolean isEmpty(String arg){
		if(isNULL(arg) || arg.length() == 0)
			return true;
		return false;
	}
	
	public static boolean isBlank(String arg){
		if(isEmpty(arg))
			return true;
		for (int i = 0; i < arg.length(); i++) {
            if ((Character.isWhitespace(arg.charAt(i)) == false)) {
                return false;
            }
        }
		return true;
	}
	
	public static boolean isNumbic(String arg){
		boolean isNumbic = false;
		if(!isBlank(arg)){
			try{
				Integer.parseInt(arg);
				isNumbic = true;
			}catch (Exception e) {}
		}
		return isNumbic;
	}
}
