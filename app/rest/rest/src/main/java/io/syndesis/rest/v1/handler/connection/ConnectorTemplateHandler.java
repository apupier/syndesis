/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.rest.v1.handler.connection;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.syndesis.dao.manager.DataManager;
import io.syndesis.model.Kind;
import io.syndesis.model.ListResult;
import io.syndesis.model.connection.Connector;
import io.syndesis.model.connection.ConnectorSettings;
import io.syndesis.model.connection.ConnectorTemplate;
import io.syndesis.rest.v1.operations.Getter;
import io.syndesis.rest.v1.operations.Lister;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Path("/custom/connectors")
@Api(tags = {"custom-connectors", "connector-template"})
@Component
public final class ConnectorTemplateHandler extends BaseConnectorGeneratorHandler
    implements Lister<ConnectorTemplate>, Getter<ConnectorTemplate> {

    protected ConnectorTemplateHandler(final DataManager dataMgr, final ApplicationContext applicationContext) {
        super(dataMgr, applicationContext);
    }

    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Creates a new Connector based on the ConnectorTemplate identified by the provided `id`  and the data given in`connectorSettings`")
    @ApiParam(name = "id", example = "swagger-connector-template")
    @ApiResponses(@ApiResponse(code = 200, response = Connector.class, message = "Newly created Connector"))
    public Connector create(@PathParam("id") @NotNull final String templateId, final ConnectorSettings connectorSettings) {

        final Connector connector = withGeneratorAndTemplate(templateId,
            (generator, template) -> generator.generate(template, connectorSettings));

        return getDataManager().create(connector);
    }

    @ApiParam(name = "id", example = "swagger-connector-template")
    @Path("/{id}")
    public ConnectorSettingsHandler connectorSettingsHandler(@PathParam("id") @NotNull final String templateId) {
        return new ConnectorSettingsHandler(templateId, getDataManager(), context);
    }

    @Override
    public ConnectorTemplate get(final String id) {
        return Getter.super.get(id);
    }

    @Override
    public ListResult<ConnectorTemplate> list(final UriInfo uriInfo) {
        return Lister.super.list(uriInfo);
    }

    @Override
    public Kind resourceKind() {
        return Kind.ConnectorTemplate;
    }
}
