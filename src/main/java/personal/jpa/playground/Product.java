package personal.jpa.playground;

import java.math.BigDecimal;
import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "product")
@SequenceGenerator(name = "PRODUCT_SEQ_GENERATOR", sequenceName = "PRODUCT_SEQ", initialValue = 1, allocationSize = 1)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ_GENERATOR")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String productName;

    private int quantity;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_group_id", insertable = false, updatable = false)
    private ProductGroup productGroup;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getProductName() {
        return productName;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }


    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public ProductGroup getProductGroup() {
        return productGroup;
    }


    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }
}
