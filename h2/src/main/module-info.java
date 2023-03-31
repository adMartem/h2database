module p3cobol.h2 {
	exports org.h2.mvstore.db;
	exports org.h2.mvstore.cache;
	exports org.h2.table;
	exports org.h2.value;
	exports org.h2.message;
	exports org.h2;
	exports org.h2.expression;
	exports org.h2.mvstore.rtree;
	exports org.h2.mode;
	exports org.h2.mvstore;
	exports org.h2.schema;
	exports org.h2.command;
	exports org.h2.server.pg;
	exports org.h2.server;
	exports org.h2.api;
	exports org.h2.bnf;
	exports org.h2.mvstore.type;
	exports org.h2.command.ddl;
	exports org.h2.index;
	exports org.h2.constraint;
	exports org.h2.server.web;
	exports org.h2.compress;
	exports org.h2.result;
	exports org.h2.jmx;
	exports org.h2.jdbc;
	exports org.h2.util;
	exports org.h2.bnf.context;
	exports org.h2.fulltext;
	exports org.h2.jdbcx;
	exports org.h2.command.dml;
	exports org.h2.security;
	exports org.h2.store;
	exports org.h2.upgrade;
	exports org.h2.store.fs;
	exports org.h2.engine;
	exports org.h2.tools;

	requires java.compiler;
	requires java.desktop;
	requires java.instrument;
	requires java.logging;
	requires java.management;
	requires java.naming;
	requires java.sql;
	requires java.transaction.xa;
	
	requires org.slf4j;

	requires p3cobol.jts.core;	
	requires p3cobol.javax.servlet.api;
	requires p3cobol.lucene.core;
	requires p3cobol.osgi.core;
	requires p3cobol.osgi.enterprise;
}