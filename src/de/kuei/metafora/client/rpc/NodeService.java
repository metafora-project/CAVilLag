package de.kuei.metafora.client.rpc;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("nodeservice")
public interface NodeService extends RemoteService{
	
	public Vector<String[]> getNodes(String language);

}
