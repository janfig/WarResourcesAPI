package com.example.warresourcesapi.resource;

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
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private LocalDate date;

    public Resource(String name, Double price, LocalDate date) {
        this.name = name;
        this.price = price;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
