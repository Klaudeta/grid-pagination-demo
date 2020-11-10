package org.vaadin.klaudeta;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.haijian.Exporter;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {


	private static final ListDataProvider<Address> dataProvider = new ListDataProvider<>(AddressMock.addresses);

	private PaginatedGrid<Address> grid;

	private TextField pageSizeTextField = new TextField("Page Size");

	private TextField paginatorSizeTextField = new TextField("Paginator Size");

	private TextField pageTextField = new TextField("Page");

	private TextField filterField = new TextField("Filter by Address");


	public MainView() {
		grid = new PaginatedGrid<>();

		grid.addColumn(Address::getId).setHeader("ID").setKey("ID");
		grid.addColumn(Address::getCountry).setHeader("Country").setKey("Country").setSortable(true);
		grid.addColumn(Address::getState).setHeader("State").setKey("State").setSortable(true);
		grid.addColumn(Address::getName).setHeader("Name").setKey("Name").setSortable(true);
		grid.addColumn(Address::getAddress).setHeader("Address").setKey("Address").setSortable(true);


		grid.setPageSize(16);
		grid.setPaginatorSize(5);

		HorizontalLayout bottomLayout = new HorizontalLayout(pageSizeTextField, paginatorSizeTextField, pageTextField, filterField);
		bottomLayout.setWidth("100%");
		Anchor download_as_excel = new Anchor(new StreamResource("my-excel.xlsx", Exporter.exportAsExcel(grid)), "Download As Excel");


		this.add(bottomLayout, download_as_excel, grid);

		grid.setDataProvider(dataProvider);

		grid.addPageChangeListener(event -> {
			Notification.show("Page changed from " + event.getOldPage() + " to " + event.getNewPage());
		});

		pageSizeTextField.addValueChangeListener(e -> {
			try {
				if (e.getValue() == null || Integer.valueOf(e.getValue()) < 1)
					return;
				grid.setPageSize(Integer.valueOf(e.getValue()));
			} catch (Exception e2) {
				Notification.show("Number format is wrong!");
			}
		});

		paginatorSizeTextField.addValueChangeListener(e -> {
			try {
				if (e.getValue() == null || Integer.valueOf(e.getValue()) < 1)
					return;

				grid.setPaginatorSize(Integer.valueOf(e.getValue()));
			} catch (Exception e2) {
				Notification.show("Number format is wrong!");
			}
		});

		pageTextField.addValueChangeListener(e -> {
			try {
				if (e.getValue() == null || Integer.valueOf(e.getValue()) < 1)
					return;
				grid.setPage(Integer.valueOf(e.getValue()));
			} catch (Exception e2) {
				Notification.show("Number format is wrong!");
			}
		});

		filterField.addValueChangeListener(event -> {
				dataProvider.setFilter(address -> {
					if(StringUtils.isEmpty(event.getValue())){
						return true;
					}
					return  address.getAddress() != null && address.getAddress().toLowerCase().contains(event.getValue().toLowerCase());
				});



		});



	}
}
