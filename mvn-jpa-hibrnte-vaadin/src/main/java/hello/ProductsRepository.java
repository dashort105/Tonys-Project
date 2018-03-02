package hello;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, String> {

	List<Products> findByProdIdStartsWithIgnoreCase(String prodId);
	List<Products> findByProdNameStartsWithIgnoreCase(String prodName);
}
