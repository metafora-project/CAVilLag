package de.kuei.metafora.client;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DnDNode extends VerticalPanel implements HasDragHandle {

	private String id;

	private String url;
	private String category;
	private String name;

	private Image image;

	public DnDNode(String pictureUrl, String category, String name, String id) {

		url = pictureUrl;
		this.category = category;
		this.name = name;
		this.id = id;

		image = new Image(url);
		image.setSize("60px", "60px");

		getElement().getStyle().setProperty("float", "left");
		
		add(image);
		setTitle(name);
//		add(new Label(this.name));

		getElement().getStyle().setBackgroundColor("#AAAAAA");
		
		getElement().getStyle().setPadding(4, Unit.PX);
		getElement().getStyle().setMargin(2, Unit.PX);

	}

	@Override
	public Widget getDragHandle() {
		return image;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}
	
	public String getId(){
		return this.id;
	}

}
