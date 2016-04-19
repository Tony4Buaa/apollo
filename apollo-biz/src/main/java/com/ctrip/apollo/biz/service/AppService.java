package com.ctrip.apollo.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctrip.apollo.biz.entity.App;
import com.ctrip.apollo.biz.entity.Audit;
import com.ctrip.apollo.biz.repository.AppRepository;
import com.ctrip.apollo.common.utils.BeanUtils;

@Service
public class AppService {

  @Autowired
  private AppRepository appRepository;

  @Autowired
  private AuditService auditService;

  @Transactional
  public void delete(long id, String owner) {
    appRepository.delete(id);

    auditService.audit(App.class.getSimpleName(), id, Audit.OP.DELETE, owner);
  }

  public List<App> findAll(Pageable pageable) {
    Page<App> page = appRepository.findAll(pageable);
    return page.getContent();
  }

  public List<App> findByName(String name) {
    return appRepository.findByName(name);
  }

  public App findOne(String appId) {
    return appRepository.findByAppId(appId);
  }

  @Transactional
  public App save(App entity) {
    App app = appRepository.save(entity);
    
    auditService.audit(App.class.getSimpleName(), app.getId(), Audit.OP.INSERT,
        app.getDataChangeCreatedBy());
    
    return app;
  }

  @Transactional
  public App update(App app) {
    App managedApp = appRepository.findByAppId(app.getAppId());
    BeanUtils.copyEntityProperties(app, managedApp);
    managedApp = appRepository.save(managedApp);
    
    auditService.audit(App.class.getSimpleName(), managedApp.getId(), Audit.OP.UPDATE,
        managedApp.getDataChangeLastModifiedBy());
    
    return managedApp;
  }
}