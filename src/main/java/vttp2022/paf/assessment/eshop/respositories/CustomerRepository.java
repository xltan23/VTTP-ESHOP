package vttp2022.paf.assessment.eshop.respositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;
import static vttp2022.paf.assessment.eshop.Utils.*;

@Repository
public class CustomerRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		// TODO: Task 3 
		// Retrieve all customer information
		SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_FIND_CUSTOMER_BY_NAME, name);
		if (!srs.next()) {
			// If no entry available return Empty box
			return Optional.empty();
		}
		// If entry found, return box of Customer
		return Optional.of(toCustomer(srs));
	}
}
