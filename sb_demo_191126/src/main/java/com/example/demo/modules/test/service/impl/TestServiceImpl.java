package com.example.demo.modules.test.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.test.dao.TestDao;
import com.example.demo.modules.test.entity.City;
import com.example.demo.modules.test.entity.Country;
import com.example.demo.modules.test.service.TestService;
@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDao testDao;
	
	@Override
	public List<City> getCitiesByCountryId(int countryId) {
		// TODO Auto-generated method stub
		
		return Optional.ofNullable(testDao.getCitiesByCountryId(countryId))
				.orElse(Collections.emptyList());
	}

	@Override
	public Country getcountryByCountryId(int countryId) {
		// TODO Auto-generated method stub
		return testDao.getcountryByCountryId(countryId);
	}

	@Override
	public Country getCountryByCountryName(String countryName) {
		// TODO Auto-generated method stub
		return testDao.getCountryByCountryName(countryName);
	}

}
