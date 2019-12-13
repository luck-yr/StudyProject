package com.example.demo.modules.test.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.modules.test.dao.TestDao;
import com.example.demo.modules.test.entity.City;
import com.example.demo.modules.test.entity.Country;
import com.example.demo.modules.test.service.TestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

	@Override
	public PageInfo<City> getCitiesByPage(int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);
		List<City> cities = Optional.ofNullable(testDao.getCitiesByPage()).orElse(Collections.emptyList());
		return new PageInfo<City>(cities);
	}

	@Override
	public City insertCityByCity(City city) {
		// TODO Auto-generated method stub
		testDao.insertCityByCity(city);
		return city;
	}

	@Override
	@Transactional
	public City updateCityByCity(City city) {
		// TODO Auto-generated method stub
		testDao.updateCityByCity(city);
		return city;
	}

	@Override
	public void deleteCity(int cityId) {
		// TODO Auto-generated method stub
		testDao.deleteCity( cityId);
	}

}
