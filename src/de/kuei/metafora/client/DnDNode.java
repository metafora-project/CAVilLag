package de.kuei.metafora.client;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
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
	
		setSize("80px", "80px");

		image = new Image(url);
		image.setSize("60px", "60px");

		getElement().getStyle().setProperty("float", "left");
		
		add(image);

		
		Label l = new Label(this.name);
		l.setSize("60px", "32px");
		l.setStyleName("card-Label");
		add(l);

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
