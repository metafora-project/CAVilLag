package de.kuei.metafora.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * In CAVilLag, each category of planning tool cards is represented in its own Panel.
 * Each CategoryPanel is visualized in a separate internal tab when CAVilLag is opened
 * in the browser.
 * @author lingnau
 *
 */
public class CategoryPanel extends SimplePanel {

	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE = "demo-InsertPanelExample";

	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE_COLUMN_COMPOSITE = "demo-InsertPanelExample-column-composite";

	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER = "demo-InsertPanelExample-container";

	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE_HEADING = "demo-InsertPanelExample-heading";

//	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE_WIDGET = "demo-InsertPanelExample-widget";

	private static final int SPACING = 2;
	private VerticalPanelWithSpacer selectedCardsPanel, availableCardsPanel;
	private PickupDragController widgetDragController;
	private String category;

	public CategoryPanel(String categoryName) {
    addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE);

		this.category = categoryName;
		this.setSize("100%", "100%");

		DemoDragHandler demoDragHandler = new DemoDragHandler(new HTML("test"));

		// use the boundary panel as this composite's widget
		AbsolutePanel boundaryPanel = new AbsolutePanel();
		boundaryPanel.setSize("100%", "100%");
		setWidget(boundaryPanel);

		// initialize our column drag controller
		PickupDragController columnDragController = new PickupDragController(boundaryPanel, false);
		columnDragController.setBehaviorMultipleSelection(false);
		columnDragController.addDragHandler(demoDragHandler);

		// initialize our widget drag controller
		widgetDragController = new PickupDragController(boundaryPanel, false);
		widgetDragController.setBehaviorMultipleSelection(false);
		widgetDragController.addDragHandler(demoDragHandler);

		// initialize horizontal panel to hold our columns
		HorizontalPanel horizontalPanel = new HorizontalPanel();

		horizontalPanel.setSize("50%", "100%");
		horizontalPanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER);
		horizontalPanel.setSpacing(SPACING);
		boundaryPanel.add(horizontalPanel);

		// initialize our column drop controller
		HorizontalPanelDropController columnDropController = new HorizontalPanelDropController(horizontalPanel);
		columnDragController.registerDropController(columnDropController);

		availableCardsPanel = new VerticalPanelWithSpacer();
		selectedCardsPanel = new VerticalPanelWithSpacer();

		// initialize a vertical panel to hold the heading and a second vertical
		// panel
		LayoutPanel availableColumnCompositePanel = new LayoutPanel();
		LayoutPanel selectedColumnCompositePanel = new LayoutPanel();
		availableColumnCompositePanel.setSize("100%", "100%");
		selectedColumnCompositePanel.setSize("100%", "100%");
		availableColumnCompositePanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_COLUMN_COMPOSITE);
		selectedColumnCompositePanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_COLUMN_COMPOSITE);

		// initialize inner vertical panel to hold individual widgets
		// VerticalPanel verticalPanel = new VerticalPanelWithSpacer();
		availableCardsPanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER);
		selectedCardsPanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER);
		availableCardsPanel.setSize("100%", "100%");
		selectedCardsPanel.setSize("100%", "100%");
		availableCardsPanel.setSpacing(SPACING);
		selectedCardsPanel.setSpacing(SPACING);
		horizontalPanel.add(availableColumnCompositePanel);
		horizontalPanel.add(selectedColumnCompositePanel);

		// initialize a widget drop controller for the current column
		VerticalPanelDropController acDropController = new VerticalPanelDropController(availableCardsPanel);
		VerticalPanelDropController scDropController = new VerticalPanelDropController(selectedCardsPanel);
		widgetDragController.registerDropController(acDropController);
		widgetDragController.registerDropController(scDropController);

		// Put together the column pieces
		Label availableHeading = new Label("available");
		availableHeading.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_HEADING);
		availableColumnCompositePanel.add(availableHeading);
		availableColumnCompositePanel.setWidgetTopHeight(availableHeading, 0, Unit.PCT, 10, Unit.PCT);
		ScrollPanel availableScroller = new ScrollPanel();
		availableScroller.setSize("100%", "100%");
		availableScroller.add(availableCardsPanel);

		LayoutPanel asAuxPanel = new LayoutPanel();
		asAuxPanel.setSize("100%", "100%");
		asAuxPanel.add(availableScroller);
		asAuxPanel.setWidgetTopBottom(availableScroller, 5, Unit.PCT, 5, Unit.PCT);
		
		availableColumnCompositePanel.add(asAuxPanel);
		availableColumnCompositePanel.setWidgetTopHeight(asAuxPanel, 10, Unit.PCT, 90, Unit.PCT);

		// make the column draggable by its heading
//		columnDragController.makeDraggable(availableColumnCompositePanel, availableHeading);

		// Put together the column pieces
		Label selectedHeading = new Label("selected");
		selectedHeading.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_HEADING);
		selectedColumnCompositePanel.add(selectedHeading);
		selectedColumnCompositePanel.setWidgetTopHeight(selectedHeading, 0, Unit.PCT, 10, Unit.PCT);
		ScrollPanel selectedScroller = new ScrollPanel();
		selectedScroller.setSize("100%", "100%");
		selectedScroller.add(selectedCardsPanel);
		
		LayoutPanel ssAuxPanel = new LayoutPanel();
		ssAuxPanel.setSize("100%", "100%");
		ssAuxPanel.add(selectedScroller);
		ssAuxPanel.setWidgetTopBottom(selectedScroller, 5, Unit.PCT, 5, Unit.PCT);
		
		selectedColumnCompositePanel.add(ssAuxPanel);
		selectedColumnCompositePanel.setWidgetTopHeight(ssAuxPanel, 10, Unit.PCT, 90, Unit.PCT);

		// make the column draggable by its heading
//		columnDragController.makeDraggable(selectedColumnCompositePanel, selectedHeading);

		// for (int row = 1; row <= ROWS; row++) {
		// // initialize a widget
		// HTML widget = new HTML("Draggable&nbsp;#" + ++count);
		// widget.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_WIDGET);
		// widget.setHeight(Random.nextInt(4) + 2 + "em");
		// verticalPanel.add(widget);
		//
		// // make the widget draggable
		// widgetDragController.makeDraggable(widget);
		// }
		// }

	}

	public void addCard(DnDNode node, boolean isSelected) {
		if (isSelected) {
			selectedCardsPanel.add(node);
		} else {
			availableCardsPanel.add(node);
		}
		widgetDragController.makeDraggable(node);
	}

	public String getCategory() {
		return category;
	}
	
	public Vector<DnDNode> getSelectedCards() {
		Vector<DnDNode> resultCards = new Vector<DnDNode>();
		Iterator<Widget> wi = selectedCardsPanel.iterator();
		Widget w;
		while (wi.hasNext()) {
			w = wi.next();
			if (w instanceof DnDNode) {
				resultCards.addElement((DnDNode)w);
			}
		}
		return resultCards;
	}
	
	public void sortCards(ArrayList<String> selectedCards) {
		Iterator<Widget> wi = selectedCardsPanel.iterator();
		Widget w;
		DnDNode node;
		while (wi.hasNext()) {
			w = wi.next();
			if (w instanceof DnDNode) {
				node = (DnDNode)w;
				selectedCardsPanel.remove(node);
				availableCardsPanel.add(node);
			}
		}
		wi = availableCardsPanel.iterator();
		while (wi.hasNext()) {
			w = wi.next();
			if (w instanceof DnDNode) {
				node = (DnDNode)w;
				if (selectedCards.contains(node.getId())) {
					availableCardsPanel.remove(node);
					selectedCardsPanel.add(node);
				}
			}
		}
	}

	public void unselectAllCards() {
		Iterator<Widget> wi = selectedCardsPanel.iterator();
		Widget w;
		DnDNode node;
		while (wi.hasNext()) {
			w = wi.next();
			if (w instanceof DnDNode) {
				node = (DnDNode)w;
				selectedCardsPanel.remove(node);
				availableCardsPanel.add(node);
			}
		}
	}

}
