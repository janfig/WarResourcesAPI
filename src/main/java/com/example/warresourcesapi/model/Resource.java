package com.example.warresourcesapi.model;

import com.example.warresourcesapi.views.ResourceView;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer"})
//@JsonFilter("resourceFilter")
public class Resource{

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