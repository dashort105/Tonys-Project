package hello;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class VaadinUI extends UI {

	private final ProductsRepository repo;

	private final ProductsEditor editor;

	final Grid<Products> grid;

	final TextField filter;
	final TextField filterProdName;

	private final Button addNewBtn;

	@Autowired
	public VaadinUI(ProductsRepository repo, ProductsEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Products.class);
		this.filter = new TextField();
		this.filterProdName = new TextField();
		this.addNewBtn = new Button("New product", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, filterProdName, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("prodId", "vendId", "prodName", "prodPrice", "prodDesc");

		
		filter.setPlaceholder("Filter by product name");
		filterProdName.setPlaceholder("Filter by product id");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listProductsProdId(e.getValue()));
		
		// Replace listing with filtered content when user changes filter
		filterProdName.setValueChangeMode(ValueChangeMode.LAZY);
		filterProdName.addValueChangeListener(e -> listProductsProdName(e.getValue()));

		// Connect selected products to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editProducts(e.getValue());
		});

		// Instantiate and edit new products the new button is clicked
		addNewBtn.addClickListener(e -> editor.editProducts(new Products( )));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listProductsProdId(filter.getValue());
			listProductsProdName(filter.getValue());
		});

		// Initialize listing
		listProductsProdId(null);
		listProductsProdName(null);
	}

	// tag::listProducts[]
	void listProductsProdId(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}
		else {
			grid.setItems(repo.findByProdIdStartsWithIgnoreCase(filterText));
		}
	}
	void listProductsProdName(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}		
		else {
			grid.setItems(repo.findByProdNameStartsWithIgnoreCase(filterText));
		}	
	}		
	// end::listProducts[]

}
