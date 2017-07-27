package com.orange.dynamic;

import com.orange.dynamic.utils.StringUtil;

public class DNSDynamic {

	public static void main(String[] args) {
		if(args.length > 0){
			String mode = args[0];
		}else{
			String aa = "123";
			System.out.println(StringUtil.isNumbic(aa));
		}
	}
}