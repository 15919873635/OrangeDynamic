package com.orange.dynamic;

import com.orange.dynamic.client.DomainClient;
import com.orange.dynamic.consts.ServerConsts;
import com.orange.dynamic.server.DomainServer;
import com.orange.dynamic.utils.StringUtil;

public class DNSDynamic {

	public static void main(String[] args) {
		Compent compent = null;
		if(args.length > 0 && !StringUtil.isBlank(args[0])){
			String mode = args[0];
			if(mode.equals(ServerConsts.SERVER_MODE))
				compent = new DomainServer();
			else
				compent = new DomainClient();
		}else
			compent = new DomainServer();
		compent.start();
	}
}