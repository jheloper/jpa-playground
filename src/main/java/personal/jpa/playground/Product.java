package personal.jpa.playground;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String productName;

    private int quantity;

    private BigDecimal price;


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
}
