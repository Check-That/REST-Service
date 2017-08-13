package de.zeppelin.checkthat.webservice.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class VersionControlFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		((HttpServletResponse)response).addHeader("Server-Version", "0.1");
		((HttpServletResponse)response).addHeader("Client-Min-Version", "0.1");
		((HttpServletResponse)response).addHeader("Client-Current-Version", "0.1");
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {	}
}
