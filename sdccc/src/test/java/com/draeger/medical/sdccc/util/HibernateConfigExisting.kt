package com.draeger.medical.sdccc.util

import com.draeger.medical.sdccc.messages.HibernateConfigBase
import com.draeger.medical.sdccc.messages.HibernateHBM2DDLAuto
import com.google.inject.Inject
import com.google.inject.Singleton

/**
 * Hibernate configuration for an existing Derby database.
 *
 * <p>
 *  Example :
 *  <pre>
 *   final Injector injector = InjectorUtil.setupInjector(
 *      false,
 *      new AbstractModule() {
 *          @Override
 *          protected void configure() {
 *              bind(TestClient.class).toInstance(mockClient);
 *              bind(HibernateConfig.class).to(HibernateConfigExistingImpl.class).in(Singleton.class);
 *          }
 *      }
 *  );
 *  </pre>
 * </p>
 */
@Singleton
class HibernateConfigExistingImpl @Inject internal constructor() :
    HibernateConfigBase(
        "path/to/Database",
        HibernateHBM2DDLAuto.NONE
    )