package hello;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.argThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductsEditorTests {

	private static final String PRODNAME = "Panther bean bag toy";
	private static final String PRODDESC = "'Panther bean bag toy, prey are not included'";

	@Mock ProductsRepository ProductsRepository;
	@InjectMocks ProductsEditor editor;

	@Test
	public void shouldStoreProductsInRepoWhenEditorSaveClicked() {
		this.editor.prodName.setValue(PRODNAME);
		this.editor.prodDesc.setValue(PRODDESC);
		productsDataWasFilled();

		this.editor.save.click();

		then(this.ProductsRepository).should().save(argThat(productsMatchesEditorFields()));
	}

	@Test
	public void shouldDeleteProductsFromRepoWhenEditorDeleteClicked() {
		this.editor.prodName.setValue(PRODNAME);
		this.editor.prodDesc.setValue(PRODDESC);
		productsDataWasFilled();

		editor.delete.click();

		then(this.ProductsRepository).should().delete(argThat(productsMatchesEditorFields()));
	}

	private void productsDataWasFilled() {
		this.editor.editProducts(new Products(PRODDESC, PRODNAME, null, null, null));
	}

	private TypeSafeMatcher<Products> productsMatchesEditorFields() {
		return new TypeSafeMatcher<Products>() {
			@Override
			public void describeTo(Description description) {}

			@Override
			protected boolean matchesSafely(Products item) {
				return PRODNAME.equals(item.getProdName()) && PRODDESC.equals(item.getProdDesc());
			}
		};
	}

}
