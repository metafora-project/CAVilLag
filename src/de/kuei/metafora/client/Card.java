package de.kuei.metafora.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * This class provides the visualization of a planning tool card in the CAVilLag.
 * @author lingnau
 *
 */
public class Card extends LayoutPanel {

	private String id;

	private String url;
	private String category;
	private String name;

	private Image image;

	public Card(String pictureUrl, String category, String name, String id) {
		
		setSize("70px", "80px");
		
		url = pictureUrl;
		this.category = category;
		this.name = name;
		this.id = id;

		image = new Image(url);
		image.setSize("60px", "60px");

		add(image);
		setWidgetTopHeight(image, 5, Unit.PX, 64, Unit.PX);
		setWidgetLeftRight(image, 4, Unit.PX, 4, Unit.PX);

		Label l = new Label(this.name);
		l.setSize("60px", "32px");
		l.setStyleName("card-Label");
		add(l);
		setWidgetTopHeight(l, 67, Unit.PX, 30, Unit.PX);
		setWidgetLeftRight(l, 4, Unit.PX, 4, Unit.PX);

		getElement().getStyle().setBackgroundColor("#AAAAAA");
		getElement().getStyle().setPadding(4, Unit.PX);
		getElement().getStyle().setMargin(2, Unit.PX);

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
