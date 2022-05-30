package com.example.warresourcesapi.utils;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Set;

public class ResourceCustomDeserializer extends StdDeserializer {

    private static final Long serialVersionUID = 1L;

    public ResourceCustomDeserializer() {
        this(Resource.class);
    }

    protected ResourceCustomDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public Resource deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode customPrices = jsonNode.get("data");
        ObjectMapper mapper = new ObjectMapper();
        Set<Price> prices =
                mapper.convertValue(
                        customPrices,
                        mapper.getTypeFactory().constructCollectionType(
                                Set.class,
                                Price.class
                        )
                );

        Resource resource = new Resource("xD");
        return resource;
    }
}
