package de.zeppelin.checkthat.webservice.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class Administration {

	@PersistenceContext
	EntityManager em;

	@RequestMapping("refresh")
	@ResponseBody
	public String refresh() {
		this.em.getEntityManagerFactory().getCache().evictAll();
		return "ok";
	}
}
