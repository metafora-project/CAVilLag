package de.kuei.metafora.client;

import java.util.HashMap;
import java.util.Vector;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;

import de.kuei.metafora.client.rpc.NodeService;
import de.kuei.metafora.client.rpc.NodeServiceAsync;

public class CAVilLag implements EntryPoint{
	
	private final NodeServiceAsync nodeService = GWT.create(NodeService.class);
	
	private PickupDragController controller;
//	private VerticalPanel cardPanel, edgePanel;
	private TabLayoutPanel main;

	private HashMap<String, CategoryPanel> categories;
	private HashMap<String, Boolean> selectedNodes;
	
	public static CAVilLag instance = null;
	
	public void onModuleLoad() {
		instance = this;
		
		categories = new HashMap<String, CategoryPanel>();
		selectedNodes = new HashMap<String, Boolean>();

		controller = new PickupDragController(RootPanel.get(), false);
		
		main = new TabLayoutPanel(3.0, Unit.EM);
		
		loadTabs();
		main.setHeight(Window.getClientHeight()+"px");
		RootPanel.get().add(main);
	}
	
	private void loadTabs(){
		nodeService.getNodes("de", new AsyncCallback<Vector<String[]>>() {
			
			@Override
			public void onSuccess(Vector<String[]> result) {
				for(String[] node : result){
					addNode(node);
				}
				loadConnectors();
				addPreviewTab();
				main.selectTab(0);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server call failed!\n"+caught.getMessage());
			}
		});
	}
	
	private void addNode(String[] node){
		if(selectedNodes.containsKey(node[5])) {
			boolean selected = selectedNodes.get(node[5]);
			addNode(node, selected);
		} else {
			addNode(node, false);
		}
	}
	
	private void addNode(String[] node, boolean selected){
		DnDNode dndnode = new DnDNode(node[0], node[3], node[2], node[5]);
		
		CategoryPanel panel = null;
		if (categories.containsKey(node[3])) {
			panel = categories.get(node[3]);
		} else {
			panel = new CategoryPanel(node[3], this.controller);
			categories.put(node[3], panel);
			main.add(panel, panel.getCategory());
			System.out.println("node panel added!");
//			main.selectTab(0);
//			cardPanel.add(panel);
		}
		
		panel.addCard(dndnode, selected);
		selectedNodes.put(dndnode.getId(), selected);
		
		this.controller.makeDraggable(dndnode);
	}
	
	protected void loadConnectors() {
		CategoryPanel connectorSelection = new CategoryPanel("Connectors", this.controller);
		System.out.println("edge panel added");
		main.add(connectorSelection,connectorSelection.getCategory());
	}

	public void setNodeState(String id,  boolean selected){
		selectedNodes.put(id, selected);
//		Window.alert(id+": "+selected);
	}

	protected void addPreviewTab() {
		DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
		Button b = new Button("Save!", new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        Window.alert("This will save...");
		      }
		    });
		p.addNorth(new HTML("header"), 2);
		p.addSouth(new HTML("footer"), 2);
		p.addWest(b, 10);
		p.add(new HTML("Here we will display the preview"));
		p.setWidth(Window.getClientWidth()/5*4+"px");
		p.setHeight(Window.getClientHeight()/5*4+"px");
		main.add(p, "preview & save");
	}
	
}
