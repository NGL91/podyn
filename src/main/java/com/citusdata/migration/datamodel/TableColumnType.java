/**
 * 
 */
package com.citusdata.migration.datamodel;

/**
 * @author marco
 *
 */
public enum TableColumnType {
	text("text"),
	numeric("numeric"),
	bytea("bytea"),
	jsonb("jsonb"),
	bool("boolean"),
	timestamp("timestamp without time zone"),
	serial("serial")
	;

	final String name;
	
	TableColumnType(String name) {
		this.name = name;
	}
	
	public static TableColumnType fromName(String name) {
		for (TableColumnType type : TableColumnType.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		
		return TableColumnType.text;
	}
	
	public String toString() {
		return name;
	}
}
