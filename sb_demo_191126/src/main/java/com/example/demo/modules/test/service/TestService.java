package com.example.demo.modules.test.service;

import java.util.List;

import com.example.demo.modules.test.entity.City;
import com.example.demo.modules.test.entity.Country;

public interface TestService {

	List<City> getCitiesByCountryId(int countryId);

	Country getcountryByCountryId(int countryId);

	Country getCountryByCountryName(String countryName);

}
