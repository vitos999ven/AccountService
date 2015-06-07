package hibernate.logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="ammounts")
public class Amount {
    @Id
    @Column(name="id", unique = true, nullable = false)
    private int id;
    @Column(name="value")
    private long value;

    
    public Amount(){}
    
    public Amount(int id, long value){
        this.id = id;
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Amount)) return false;
        Amount other = (Amount)obj;
        return ((this.getId() == other.getId()) && (this.getValue() == other.getValue()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
    
}
