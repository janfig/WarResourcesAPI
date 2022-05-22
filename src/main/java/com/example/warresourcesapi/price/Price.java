package com.example.warresourcesapi.price;

import com.example.warresourcesapi.resource.Resource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Resource resource;

    private Double price;
    private LocalDate date;

    public Price(Resource resource, Double price, LocalDate date) {
        this.resource = resource;
        this.price = price;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", resource=" + resource +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
