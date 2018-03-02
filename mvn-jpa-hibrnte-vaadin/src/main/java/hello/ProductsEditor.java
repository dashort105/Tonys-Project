package hello;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in VaadinUI.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX. See e.g. AbstractForm in Viritin
 * (https://vaadin.com/addon/viritin).
 */
@SpringComponent
@UIScope
public class ProductsEditor extends VerticalLayout {

	private final ProductsRepository repository;

	/**
	 * The currently edited products
	 */
	private Products products;

	
	TextField prodId = new TextField("Product ID");
	TextField vendId = new TextField("Vendor ID");
	TextField prodName = new TextField("Product Name");
	TextField prodPrice = new TextField("Product Price");
	TextField prodDesc = new TextField("Product Description");
	
	
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Products> binder = new Binder<>(Products.class);

	@Autowired
	public ProductsEditor(ProductsRepository repository) {
		this.repository = repository;

		addComponents(prodId, vendId, prodName, prodPrice, prodDesc, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> repository.save(products));
		delete.addClickListener(e -> repository.delete(products));
		cancel.addClickListener(e -> editProducts(products));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editProducts(Products p) {
		if (p == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = p.getProdId() !=null;
		if (persisted) {
			// Find fresh entity for editing
			products = repository.findOne(p.getProdId());
		}
		else {
			products = p;
		}
		cancel.setVisible(persisted);

		// Bind products properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(products);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		prodId.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}
