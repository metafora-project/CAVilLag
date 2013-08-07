package de.kuei.metafora.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the drop controller used to make the planning tool cards DnD'able in CAVilLag
 * @author lingnau
 *
 */
public class CategoryPanelDropController extends FlowPanelDropController {

	private boolean selected;
	private String category;

	public CategoryPanelDropController(FlowPanel dropTarget, String category, boolean selected) {
		super(dropTarget);
		this.selected = selected;
		this.category = category;
	}

	@Override
	public void onDrop(DragContext context) {
		super.onDrop(context);

		Widget w = context.draggable;
		if (w instanceof DnDNode) {
			DnDNode node = (DnDNode) w;
			CAVilLag.instance.setNodeState(node.getId(), this.selected);
		}
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		Widget w = context.draggable;
		if (w instanceof DnDNode) {
			DnDNode node = (DnDNode) w;
			Window.alert(node.getId() + " " + node.getName());
			
			if(!this.category.equals(node.getCategory())){
				throw new VetoDragException();
			}
			
		}
	}

}
