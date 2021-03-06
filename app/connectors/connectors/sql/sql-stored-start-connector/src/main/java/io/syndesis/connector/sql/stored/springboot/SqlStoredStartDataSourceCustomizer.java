/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.connector.sql.stored.springboot;

import org.apache.camel.component.connector.ConnectorCustomizer;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.syndesis.connector.sql.stored.SqlStoredStartConnectorComponent;

@Configuration
@EnableConfigurationProperties(SqlStoredStartConnectorConnectorConfiguration.class)
public class SqlStoredStartDataSourceCustomizer implements ConnectorCustomizer<SqlStoredStartConnectorComponent> {

    @Autowired
    private SqlStoredStartConnectorConnectorConfiguration configuration;

    @Override
    public void customize(SqlStoredStartConnectorComponent component) {
        if (configuration.getDataSource() == null) {
            BasicDataSource ds = new BasicDataSource();
            ObjectHelper.ifNotEmpty(configuration.getUser(), ds::setUsername);
            ObjectHelper.ifNotEmpty(configuration.getPassword(), ds::setPassword);
            ObjectHelper.ifNotEmpty(configuration.getUrl(), ds::setUrl);

            component.addOption("dataSource", ds);
        }
    }
}