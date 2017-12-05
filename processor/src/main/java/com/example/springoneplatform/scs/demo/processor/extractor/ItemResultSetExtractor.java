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


import com.example.springoneplatform.scs.demo.model.etl.ItemPayload;
import com.example.springoneplatform.scs.demo.model.etl.PayloadWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSetExtractor for extracting item data
 *
 * @author Jeff Cherng
 */
public class ItemResultSetExtractor implements ResultSetExtractor<PayloadWrapper<ItemPayload>> {

	@Override
	public PayloadWrapper<ItemPayload> extractData(ResultSet rs) throws SQLException, DataAccessException {
		ItemPayload itemPayload = null;
		if (rs.next()) {
			itemPayload = new ItemPayload();
			itemPayload.setId(rs.getString("ID"));
			itemPayload.setName(rs.getString("NAME"));
			itemPayload.setPrice(rs.getBigDecimal("PRICE"));
			itemPayload.setDescription(rs.getString("DESCRIPTION"));
		}
		return new PayloadWrapper<>(itemPayload);
	}
}
