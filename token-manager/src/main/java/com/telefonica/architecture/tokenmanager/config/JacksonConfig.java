package com.telefonica.architecture.tokenmanager.config;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.json.bind.JsonbConfig;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig  implements ContextResolver<Jsonb> {

    private static final String TEMPLATE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Override
    public Jsonb getContext(Class objectType) {
        JsonbConfig config = new JsonbConfig();
        config.withDateFormat(TEMPLATE, null);
        return JsonbBuilder.create(config);
    }

}