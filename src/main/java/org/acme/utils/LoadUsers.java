package org.acme.utils;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import org.acme.controllers.auth.AccessEntity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;


@Singleton
public class LoadUsers {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        AccessEntity ae = new AccessEntity();
        ae.deleteAll();
        ae.setAppname("tdr_online");
        ae.setPassword(BcryptUtil.bcryptHash("P@ssw0rd"));
        ae.setUsername("tdr");
        ae.persist();


        AccessEntity ae2 = new AccessEntity();
        ae2.setAppname("admin");
        ae2.setPassword(BcryptUtil.bcryptHash("P@ssw0rdOnix"));
        ae2.setUsername("onix");
        ae2.persist();
        
    }
}
