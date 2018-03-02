package hello;

import javax.persistence.*;
//import javax.validation.constraints.NotNull;


@Entity
@Table(name = "products")
public class Products {
	
	@Id
	//@GeneratedValue
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	//@NotNull
	@Column(name="prod_id")
	  private String prodId;
	  
	@Column(name="vend_id")
		private String vendId;
	  
	@Column(name="prod_name")
	  private String prodName;

	@Column(name="prod_price")
	  private String prodPrice;
	
	@Column(name="prod_desc")
	  private String prodDesc;
	
	
	
	protected Products() {
	}
	
	public Products(Integer Id, String prodId, String vendId, String prodName, String prodPrice, String prodDesc) {
		
		this.id = id;
		this.prodId = prodId;
		this.vendId = vendId;
		this.prodName = prodName;
		this.prodPrice = prodPrice;
		this.prodDesc = prodDesc;
	}
	
	public Integer Id() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public String getProdId() {
	    return prodId;
	  }

	  public void setProdid(String prodId) {
	    this.prodId = prodId;
	  }

	  public String getVendId() {
	    return vendId;
	  }
	  
	  public void setVendId(String vendId) {
	    this.vendId = vendId;
	  }
	  	  
	  public String getProdName() {
		    return prodName;
		  }

		  public void setProdName(String prodName) {
		    this.prodName = prodName;
		  }
		  
	  public String getProdPrice() {
	    return prodPrice;
	  }

	  public void setProdPrice(String prodPrice) {
	    this.prodPrice = prodPrice;
	  }
	  
	  public String getProdDesc() {
		    return prodDesc;
		  }

		  public void setProdDesc(String prodDesc) {
		    this.prodDesc = prodDesc;
		  }
	  
	@Override
	public String toString() {
		return "Product [Id = " + id + ", Product Id = " + prodId + ", Vendor ID = " + vendId + ", Product Name = " + prodName + ", Product Price = " + prodPrice + " , Product Description = " + prodDesc + "]";

}
}	
