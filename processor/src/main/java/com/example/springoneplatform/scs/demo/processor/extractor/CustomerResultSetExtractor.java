/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.springoneplatform.scs.demo.processor.extractor;

import com.example.springoneplatform.scs.demo.model.etl.CustomerPayload;
import com.example.springoneplatform.scs.demo.model.etl.PayloadWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * ResultSetExtractor for extracting customer data
 *
 * @author Jeff Cherng
 */
public class CustomerResultSetExtractor implements ResultSetExtractor<PayloadWrapper<CustomerPayload>> {

	@Override
	public PayloadWrapper<CustomerPayload> extractData(ResultSet rs) throws SQLException, DataAccessException {
		CustomerPayload customerPayload = null;
		if (rs.next()) {
			customerPayload = new CustomerPayload();
			customerPayload.setId(rs.getString("ID"));
			customerPayload.setName(rs.getString("NAME"));
		}
		return new PayloadWrapper<>(customerPayload);
	}
}
