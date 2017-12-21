package com.jinloes;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by jinloes on 1/27/17.
 */
@Component
public class ApplicationEventListeners {
	private ConsulClient consulClient;
	private InetUtils inetUtils;

	@Autowired
	public ApplicationEventListeners(ConsulClient consulClient, InetUtils inetUtils) {
		this.consulClient = consulClient;
		this.inetUtils = inetUtils;
	}

	@EventListener
	public void handle(ApplicationReadyEvent event) {
		/*// register new service with associated health check
		NewService newService = new NewService();
		newService.setId("myapp_01");
		newService.setName("myapp");
		newService.setPort(8181);

		InetUtils.HostInfo hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
		NewService.Check serviceCheck = new NewService.Check();
		serviceCheck.setHttp("http://" + hostInfo.getHostname() + ":8181/health");
		serviceCheck.setInterval("10s");
		newService.setCheck(serviceCheck);

		consulClient.agentServiceRegister(newService);*/
	}
}
