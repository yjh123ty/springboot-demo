package com.yoga.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoga.demo.domain.syslog.SysLog;
import com.yoga.demo.mapper.syslog.SysLogMapper;
import com.yoga.demo.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService{
	
	@Autowired
	private SysLogMapper sysLogMapper;
	
	@Override
	public void saveLog(SysLog syslog) {
		sysLogMapper.insert(syslog);
	}
	
	
}
