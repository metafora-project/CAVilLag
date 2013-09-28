package de.kuei.metafora.client.rpc;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NodeServiceAsync {

	void getNodes(String language, AsyncCallback<Vector<String[]>> callback);

}
