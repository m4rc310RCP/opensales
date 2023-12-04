package foundation.cmo.opensales.graphql.strategies;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * The Class MPhysicalNamingImpl.
 */
public class MPhysicalNamingImpl extends PhysicalNamingStrategyStandardImpl{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * To physical sequence name.
	 *
	 * @param name    the name
	 * @param context the context
	 * @return the identifier
	 */
	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	/**
	 * To physical schema name.
	 *
	 * @param name    the name
	 * @param context the context
	 * @return the identifier
	 */
	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	/**
	 * To physical catalog name.
	 *
	 * @param name    the name
	 * @param context the context
	 * @return the identifier
	 */
	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	/**
	 * To physical column name.
	 *
	 * @param name    the name
	 * @param context the context
	 * @return the identifier
	 */
	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return apply(name, context);
	}

	/**
	 * To physical table name.
	 *
	 * @param name    the name
	 * @param context the context
	 * @return the identifier
	 */
	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		name = apply(name, context);
		return Identifier.toIdentifier(name.getCanonicalName().toUpperCase(), true);
	}
	
	
	
	/**
	 * Apply.
	 *
	 * @param name    the name
	 * @param context the context
	 * @return the identifier
	 */
	public Identifier apply(Identifier name, JdbcEnvironment context) {
		throw new UnsupportedOperationException("No imlementation for MPhysicalNamingImpl.apply");
	}

}
