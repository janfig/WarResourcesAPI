package com.example.warresourcesapi.resource;

import com.example.warresourcesapi.price.Price;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer"})
//@JsonFilter("resourceFilter")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ResourceView.Basic.class)
    private Long id;

    @JsonView(ResourceView.Basic.class)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(ResourceView.Full.class)
    private Set<Price> prices;


    public Resource(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prices=" + prices +
                '}';
    }
}
