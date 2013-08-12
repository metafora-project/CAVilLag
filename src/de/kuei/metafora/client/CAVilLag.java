package de.kuei.metafora.client;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import de.kuei.metafora.client.rpc.ChallengeModel;
import de.kuei.metafora.client.rpc.NodeService;
import de.kuei.metafora.client.rpc.NodeServiceAsync;

/**
 * Welcome to the main class for the CAVilLag application in Metafora. As it says, it 
 * is the entry point for CAVilLag.
 * @author lingnau
 *
 */
public class CAVilLag implements EntryPoint {

	private final NodeServiceAsync nodeService = GWT.create(NodeService.class);

	private PickupDragController controller;
	private TabLayoutPanel main;
	
	private ChallengesPanel challengePanel;
	private Document doc;
	private ResultPanel rPanel;

	private HashMap<String, CategoryPanel> categoryMap;
	private HashMap<String, Boolean> selectedNodes;
	private HashMap<String, String[]> nodeMap;
	private HashMap<String, String> categoryNameIdMap;
//	private ChallengeTemplate templateProperties;

	public static CAVilLag instance = null;

	public void onModuleLoad() {
		instance = this;

		categoryMap = new HashMap<String, CategoryPanel>();
		selectedNodes = new HashMap<String, Boolean>();
		nodeMap = new HashMap<String, String[]>();
		categoryNameIdMap = new HashMap<String, String>();

		controller = new PickupDragController(RootPanel.get(), false);

		main = new TabLayoutPanel(3.0, Unit.EM);
		main.setSize("100%", "100%");
		main.setAnimationDuration(1000);
		main.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem().equals(main.getWidgetIndex(rPanel))) {
					rPanel.refreshView();
				}

			}

		});
		
		addChallengeTab();
		loadTabs();
		RootLayoutPanel.get().add(main);
	  Window.enableScrolling(true);
	}

	private void loadTabs() {
		nodeService.getNodes("en", new AsyncCallback<Vector<String[]>>() {

			@Override
			public void onSuccess(Vector<String[]> result) {
				for (String[] node : result) {
					addNode(node);
				}
				loadConnectors();
				rPanel = new ResultPanel();
				main.add(rPanel, "preview & save");
				main.selectTab(0);
//				writeTemplate();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server call failed!\n" + caught.getMessage());
			}
		});
	}

	private void addNode(String[] node) {
		if (selectedNodes.containsKey(node[5])) {
			boolean selected = selectedNodes.get(node[5]);
			addNode(node, selected);
		} else {
			addNode(node, false);
		}
	}

	private void addNode(String[] node, boolean selected) {
		DnDNode dndnode = new DnDNode(node[0], node[3], node[2], node[5]);

		CategoryPanel panel = null;
		if (categoryMap.containsKey(node[3])) {
			panel = categoryMap.get(node[3]);
		} else {
			panel = new CategoryPanel(node[3]/*, this.controller*/);
			categoryMap.put(node[3], panel);
			categoryNameIdMap.put(node[3], node[6]);
			main.add(panel, panel.getCategory());
			System.out.println("node panel added!");
			// main.selectTab(0);
			// cardPanel.add(panel);
		}
		

		panel.addCard(dndnode, selected);
		selectedNodes.put(dndnode.getId(), selected);
		nodeMap.put(dndnode.getId(), node);
//		templateProperties.setProperty(dndnode.getId(), new Boolean(selected).toString());

		this.controller.makeDraggable(dndnode);
	}

	private void loadConnectors() {
		CategoryPanel connectorSelection = new CategoryPanel("Connectors");
		System.out.println("edge panel added");
		main.add(connectorSelection, connectorSelection.getCategory());
	}

	public void setNodeState(String id, boolean selected) {
		selectedNodes.put(id, selected);
	}
	
	protected void updateChallengeTab() {
		nodeService.getChallenges(new AsyncCallback<Vector<ChallengeModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server call failed!\n" + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<ChallengeModel> result) {
				challengePanel.updateChallenges(result);
			}});
	}

	protected void addChallengeTab() {
		nodeService.getChallenges(new AsyncCallback<Vector<ChallengeModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server call failed!\n" + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<ChallengeModel> result) {
				challengePanel = new ChallengesPanel(result);
				main.add(challengePanel, "Challenges");
			}
		});
	}

	public void saveTemplate() {
		final ChallengeModel model = challengePanel.getSelectedChallenge();
    final DialogBox db = new DialogBox();
//    final Boolean saveFlag = new Boolean(false);
    
		if (model == null) {
			Window.alert("please select a challenge!");
			return;
		}
    LayoutPanel panel = new LayoutPanel();

    // this is in case we haven't defined anything yet for the challenge
    if ((model.getLastUsed() == null) || model.getLastUsed().isEmpty() 
    		 || (model.getTemplate() == null) || model.getTemplate().isEmpty()) {
			writeTemplateToDB(model.getChallengeId());
			return;
    }
    
		Date lastUsedDate = new Date(Long.parseLong(model.getLastUsed()));
	  Date createdDate = new Date(Long.parseLong(CAVilLag.instance.getTimestamp(model.getTemplate())));
		if (lastUsedDate.after(createdDate)) {
			db.setHTML("<h2>ATTENTION!</h2>");
			HTML info = new HTML("<p>A visual language for this challenge has been created on "
					+ DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(createdDate) + "</p>"
					+ "<p>The last time this challenge has been loaded in the Planning Tool was on "
					+ DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(lastUsedDate)+"</p>"
					+ "<p>Saving a new template may cause irreversible damage to an ongoing experiment!</p>" 
					+ "<p><b>Please do not press save if you are not sure!</b></p>");
			panel.add(info);
			panel.setWidgetTopHeight(info, 0, Unit.PCT, 80, Unit.PCT);
			
	    Button ok = new Button("SAVE");
	    ok.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					db.hide();
					writeTemplateToDB(model.getChallengeId());
				}
			});
	    panel.add(ok);
	    panel.setWidgetLeftWidth(ok, 10, Unit.PCT, 30, Unit.PCT);  // Left panel
	    panel.setWidgetBottomHeight(ok, 0, Unit.PCT, 20, Unit.PCT);
	    Button cancel = new Button("CANCEL");
	    cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					db.hide();
				}
			});
	    panel.add(cancel);
	    panel.setWidgetRightWidth(cancel, 10, Unit.PCT, 30, Unit.PCT); // Right panel	    
	    panel.setWidgetBottomHeight(cancel, 0, Unit.PCT, 20, Unit.PCT);
	    panel.setSize("300px", "200px");
	    db.setWidget(panel);
      db.setModal(true);
      db.center();
	  } else {
			writeTemplateToDB(model.getChallengeId());
	  }
	}
	
	/**
	 * Creates a new challenge and adds it to the database
	 * @param name
	 * @param url
	 */
	public void createNewChallenge(String name, String url) {
		nodeService.addNewChallenge(name, url, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("create new challenge failed");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					Window.alert("sucessfully created new challenge");
					challengePanel.resetChallengeCreatorPanel();
				} else {
					Window.alert("Please check URL!");
				}
			}});
	}
	
	/**
	 * Creates a new challenge and adds it to the database
	 * @param name
	 * @param url
	 */
	public void createNewChallenge(String name, String url, String template) {
		nodeService.addNewChallenge(name, url, template, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("copy challenge failed");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					Window.alert("challenge sucessfully copied");
					challengePanel.resetChallengeCreatorPanel();
				} else {
					Window.alert("Please check URL!");
				}
			}});
	}
	
	/**
	 * Write the template, i.e. the selection of cards that will be loaded in the planning tool
	 * to the database and connect it with the challenge ID. In the future this challenge will be loaded
	 * in the planning tool using the template (i.e. the selection of cards composed with CAVilLag) 
	 * @param challengeID
	 */
	public void writeTemplateToDB(int challengeID) {
		doc = rPanel.getXml();
		nodeService.updateVisualLanguage(challengeID, doc.toString(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Visual language saving failed");
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Visual language saved");
			}});
	}
	
	/**
	 * get the timestamp when the selection has been created
	 * @param template
	 * @return
	 */
	public String getTimestamp(String template) {
    Document messageDom = XMLParser.parse(template);
    
    Node templateNode = messageDom.getElementsByTagName("template").item(0);
    if (templateNode != null) {
	    return ((Element)templateNode).getAttribute("time"); 
    }
    return null;
	}
	
	public Set<String> getCategories() {
		return categoryMap.keySet();
	}
	
	public Vector<String[]> getSelectedNodes(String categoryName) {
		Vector<String[]> categoryNodes = new Vector<String[]>();
		for (DnDNode dndNode : categoryMap.get(categoryName).getSelectedCards()) {
			categoryNodes.add(nodeMap.get(dndNode.getId()));
		}
		return categoryNodes;
	}

	public Boolean isNodeSelected(String name) {
		return selectedNodes.get(name);
	}
	
	public String getCategoryID(String name) {
		return categoryNameIdMap.get(name);
	}

	/**
	 * update the URL connected with the challenge ID
	 * @param challengeID
	 * @param url
	 */
	public void updateChallengeUrl(int challengeID, String url) {
		nodeService.updateChallengeUrl(challengeID, url, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("update challenge URL failed");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
//					Window.alert("sucessfully updated challenge URL");
					challengePanel.resetChallengeCreatorPanel();
				} else {
					Window.alert("Please check URL!");
				}
			}});
	}

	public void updateChallengeName(int challengeID, String name) {
		nodeService.updateChallengeName(challengeID, name, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("update challenge URL failed");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					challengePanel.resetChallengeCreatorPanel();
				} else {
					Window.alert("Please check Name!");
				}
			}});
	}

	public void readTemplate(String template) {
		if (template==null || !template.startsWith("<?xml")) {
			for (CategoryPanel cp : categoryMap.values()) {
				cp.unselectAllCards();
			}
			rPanel.refreshView();
			return;
		}
		ArrayList<String> selectedCards;
		Document xmlDoc = XMLParser.parse(template);
		Element root = xmlDoc.getDocumentElement();
		NodeList results = root.getChildNodes();
		HashMap<String, ArrayList<String>> selectedCardsMap = new HashMap<String, ArrayList<String>>();
		for (int counter=0; counter<results.getLength(); ++counter) {
			Element categoryElement = (Element) results.item(counter);
			String categoryID = categoryElement.getAttribute("id");
			NodeList cards = categoryElement.getElementsByTagName("card");
			selectedCards = new ArrayList<String>();
			for (int i=0; i<cards.getLength(); ++i) {
				Element card = (Element)cards.item(i);
				selectedCards.add(card.getAttribute("id"));
			}
			selectedCardsMap.put(categoryID, selectedCards);
		}
		for (String categoryName : categoryMap.keySet()) {
			CategoryPanel cp = categoryMap.get(categoryName);
			selectedCards = selectedCardsMap.get(getCategoryID(categoryName));
			if (selectedCards != null) {
				cp.sortCards(selectedCards);
			}
		}
		rPanel.refreshView();
	}
	
}
