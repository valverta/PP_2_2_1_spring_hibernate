package hiber.model;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series")
    private int series;
    @Column(name = "model")
    private String model;
    public Car() {

    }
    public Car(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model;
    }

    public int getSeries() {
        return series;
    }

    public String getModel() {
        return model;
    }
}
