package io.helidon.examples.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mybatis")
public class MyBatisResource {

	private final SqlSessionFactory factory;

	@Inject
	public MyBatisResource(final SqlSessionFactory factory) {
		this.factory = factory;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String get() {
		System.out.println(factory);

		try(final var s = factory.openSession()) {


			System.out.println(s);
		}

		return "OK";
	}
}
