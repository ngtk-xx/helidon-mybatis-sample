/*
 * Copyright (c) 2018, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.examples.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.eclipse.microprofile.config.Config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

/**
 * Provider for SqlSessionFactory
 */
@ApplicationScoped
public class SqlSessionFactoryProvider {

	private static final String KEY_DB_DRIVER = "db.driver";
	private static final String KEY_DB_URL = "db.url";
	private static final String KEY_DB_USER = "db.user";
	private static final String KEY_DB_PASSWORD = "db.password";

	private static final String XML = "mybatis.xml";

	private final Config config;

	@Inject
	public SqlSessionFactoryProvider(Config config) {
		this.config = config;
	}

	@Produces
	@ApplicationScoped
	public SqlSessionFactory provide() {

		final var prop = new Properties();
		prop.putAll(Map.of(
				KEY_DB_DRIVER, config.getValue(KEY_DB_DRIVER, String.class),
				KEY_DB_URL, config.getValue(KEY_DB_URL, String.class),
				KEY_DB_USER, config.getValue(KEY_DB_USER, String.class),
				KEY_DB_PASSWORD, config.getValue(KEY_DB_PASSWORD, String.class)
			));

		//以下はMyBatisの初期化処理
		try(final InputStream inputStream = Resources.getResourceAsStream(XML)) {

			return new SqlSessionFactoryBuilder().build(inputStream, prop);

		} catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
