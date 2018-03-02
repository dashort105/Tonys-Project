package hello;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.*;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.boot.VaadinAutoConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VaadinUITests.Config.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class VaadinUITests {

	@Autowired ProductsRepository repository;

	VaadinRequest vaadinRequest = Mockito.mock(VaadinRequest.class);

	ProductsEditor editor;

	VaadinUI vaadinUI;

	@Before
	public void setup() {
		this.editor = new ProductsEditor(this.repository);
		this.vaadinUI = new VaadinUI(this.repository, editor);
	}

	@Test
	public void shouldInitializeTheGridWithProductsRepositoryData() {
		int customerCount = (int) this.repository.count();

		vaadinUI.init(this.vaadinRequest);

		then(vaadinUI.grid.getColumns()).hasSize(3);
		then(getProductsInGrid()).hasSize(productsCount);
	}

	private List<Products> getProductInGrid() {
		ListDataProvider<Products> ldp = (ListDataProvider) vaadinUI.grid.getDataProvider();
		return new ArrayList<>(ldp.getItems());
	}

	@Test
	public void shouldFillOutTheGridWithNewData() {
		int initialProductsCount = (int) this.repository.count();
		this.vaadinUI.init(this.vaadinRequest);
		productsDataWasFilled(editor, "Marcin", "Grzejszczak");

		this.editor.save.click();

		then(getProductsInGrid()).hasSize(initialProductsCount + 1);

		then(getProductsInGrid().get(getProductsInGrid().size() - 1))
			.extracting("firstName", "lastName")
			.containsExactly("Marcin", "Grzejszczak");

	}

	@Test
	public void shouldFilterOutTheGridWithTheProvidedLastName() {
		this.vaadinUI.init(this.vaadinRequest);
		this.repository.save(new Products());

		vaadinUI.listProducts("Long");

		then(getProductsInGrid()).hasSize(1);
		then(getProductsInGrid().get(getProductsInGrid().size() - 1))
			.extracting("prodName")
			.containsExactly("Josh");
	}

	@Test
	public void shouldInitializeWithInvisibleEditor() {
		this.vaadinUI.init(this.vaadinRequest);

		then(this.editor.isVisible()).isFalse();
	}

	@Test
	public void shouldMakeEditorVisible() {
		this.vaadinUI.init(this.vaadinRequest);
		Products first = getProductsInGrid().get(0);
		this.vaadinUI.grid.select(first);

		then(this.editor.isVisible()).isTrue();
	}

	private void productsDataWasFilled(ProductsEditor editor, String prodName,
			String prodName) {
		this.editor.prodName.setValue(prodName);
		//this.editor.lastName.setValue(lastName);
		editor.editProducts(new Products(prodName));
	}

	@Configuration
	@EnableAutoConfiguration(exclude = VaadinAutoConfiguration.class)
	static class Config {

		@Autowired
		ProductsRepository repository;

		@PostConstruct
		public void initializeData() {
			this.repository.save(new Products("Jack", "Bauer"));
			/*this.repository.save(new Products("Chloe", "O'Brian"));
			this.repository.save(new Products("Kim", "Bauer"));
			this.repository.save(new Products("David", "Palmer"));
			this.repository.save(new Products("Michelle", "Dessler"));*/
		}
	}
}
