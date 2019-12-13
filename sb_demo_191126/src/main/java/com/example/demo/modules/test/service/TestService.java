package com.example.demo.modules.test.service;

import java.util.List;

import com.example.demo.modules.test.entity.City;
import com.example.demo.modules.test.entity.Country;
import com.github.pagehelper.PageInfo;

public interface TestService {

	List<City> getCitiesByCountryId(int countryId);

	Country getcountryByCountryId(int countryId);

	Country getCountryByCountryName(String countryName);

	PageInfo<City> getCitiesByPage(int currentPage, int pageSize);

	City insertCityByCity(City city);

	City updateCityByCity(City city);

	void deleteCity(int cityId);

}
