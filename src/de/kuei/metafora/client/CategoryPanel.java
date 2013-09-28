package de.kuei.metafora.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CategoryPanel extends LayoutPanel {
	private String categoryName;

	// To contain the selected cards:
	private FlowPanel selected;
	// To contain the cards that still can be selected:
	private FlowPanel unSelected;

	private FlowPanelDropController selectedController;
	private FlowPanelDropController unSelectedController;

	public CategoryPanel(String categoryName, PickupDragController dragController) {
//		setWidth("100%");
		// setHeight("10%");
		this.categoryName = categoryName;

//		this.add(new HTML(categoryName));

//		HorizontalPanel content = new HorizontalPanel();
//		this.setWidth("100%");
//		content.setHeight("10%");
//		this.add(content);

		selected = new FlowPanel();
		unSelected = new FlowPanel();

		this.add(selected);
		this.add(unSelected);
	    this.setWidgetLeftWidth(selected, 0, Style.Unit.PCT, 50, Style.Unit.PCT);
	    this.setWidgetRightWidth(unSelected, 0, Style.Unit.PCT, 50, Style.Unit.PCT);

//		selected.setWidth(Window.getClientWidth()/5*2+"px");
//		selected.setHeight(Window.getClientHeight()+"px");
//		unSelected.setWidth(Window.getClientWidth()/5*2+"px");
//		unSelected.setHeight(Window.getClientHeight()+"px");

		selected.setTitle("selected");
		unSelected.setTitle("unSelected");

		selected.getElement().getStyle().setBackgroundColor("#A0FFAA");
		unSelected.getElement().getStyle().setBackgroundColor("#CCCCCC");

		selectedController = new CategoryPanelDropController(selected, categoryName, true);
		unSelectedController = new CategoryPanelDropController(unSelected, categoryName, false);

		dragController.registerDropController(selectedController);
		dragController.registerDropController(unSelectedController);

	}

	public void addCard(DnDNode node, boolean isSelected) {
		if (isSelected)
			selected.add(node);
		else
			unSelected.add(node);
	}

	public boolean isSelected(DropController controller) {
		if (controller.equals(selectedController)) {
			return true;
		} else {
			return false;
		}
	}

	public String getCategory() {
		return categoryName;
	}

}
