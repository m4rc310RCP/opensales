package foundation.cmo.opensales.graphql.strategies;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class MPhysicalNamingImpl extends PhysicalNamingStrategyStandardImpl{

	private static final long serialVersionUID = 1L;
	
	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		name = apply(name, context);
		return Identifier.toIdentifier(name.getCanonicalName().toUpperCase(), true);
	}
	
	
	
	public Identifier apply(Identifier name, JdbcEnvironment context) {
		throw new UnsupportedOperationException("No imlementation for MPhysicalNamingImpl.apply");
	}

}
