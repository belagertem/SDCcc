package com.draeger.medical.sdccc.messages

/**
 * Different hibernate hbm2ddl auto property values
 */
enum class HibernateHBM2DDLAuto(val value: String) {
    NONE("none"), // disables the hbm2ddl.auto tool
    CREATE_ONLY("create-only"), // generate the database schema from the entity model
    DROP("drop"), // drop the database schema using the entity model as a reference
    CREATE("create"), // drop the database schema and recreate it afterward using the entity model as a reference
    CREATE_DROP("create-drop"), // drop the database schema and recreate it afterward using the entity model as a reference, upon closing Hibernate database schema will be dropped again
    VALIDATE("validate"), // validate the underlying database schema against the entity mappings
    UPDATE("update"); // update the database schema by comparing the existing schema with the entity mappings
}
