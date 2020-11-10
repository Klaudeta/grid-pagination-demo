package org.vaadin.klaudeta;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.haijian.Exporter;

/**
 * The main view contains a button and a click listener.
 */
@Push
@Route("exporter")
public class ExporterMainView extends VerticalLayout {


	private static final ListDataProvider<Address> dataProvider = new ListDataProvider<>(AddressMock.addresses);

	private Grid<Address> grid;

	private TextField pageSizeTextField = new TextField("Page Size");

	private TextField paginatorSizeTextField = new TextField("Paginator Size");

	private TextField pageTextField = new TextField("Page");

	private TextField filterField = new TextField("Filter by Address");


	public ExporterMainView() {
		grid = new Grid<>();

		grid.addColumn(Address::getId).setHeader("ID").setKey("id");
		grid.addColumn(Address::getCountry).setHeader("Country").setKey("country").setSortable(true);
		grid.addColumn(Address::getState).setHeader("State").setKey("state").setSortable(true);
		grid.addColumn(Address::getName).setHeader("Name").setKey("name").setSortable(true);
		grid.addColumn(Address::getAddress).setHeader("Address").setKey("address").setSortable(true);

		grid.setDataProvider(dataProvider);
		grid.setPageSize(16);

		HorizontalLayout bottomLayout = new HorizontalLayout(pageSizeTextField, paginatorSizeTextField, pageTextField, filterField);
		bottomLayout.setWidth("100%");
		Anchor download_as_excel = new Anchor(new StreamResource("my-excel.xlsx", Exporter.exportAsExcel(grid)), "Download As Excel");


		this.add(bottomLayout, download_as_excel, grid);




	}
}
