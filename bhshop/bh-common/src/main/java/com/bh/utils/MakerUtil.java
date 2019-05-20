package com.bh.utils;

import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.javafaker.Internet;

public class MakerUtil {
	public static void main(String[] args) {
		Faker faker = new Faker(new Locale("zh-CN"));
	/*	String name = faker.name().fullName(); // Miss Samanta Schmidt
		String firstName = faker.name().firstName(); // Emory
		String userName = faker.name().username();
		System.out.println("userName--->"+userName);
		String lastName = faker.name().lastName(); // Barton
		String streetAddress = faker.address().streetAddress();
		String fullAddress = faker.address().fullAddress();
		String city = faker.address().city();
		String cityName = faker.address().cityName();
		String secondaryAddress = faker.address().secondaryAddress();
		String zipCode = faker.address().zipCode();
		System.out.println("name--->"+name);
		System.out.println("firstName--->"+firstName);
		System.out.println("lastName--->"+lastName);
		System.out.println("streetAddress--->"+streetAddress);
		
		System.out.println("fullAddress--->"+fullAddress);
		System.out.println("city--->"+city);
		System.out.println("cityName--->"+cityName);
		System.out.println("secondaryAddress--->"+secondaryAddress);
		System.out.println("zipCode--->"+zipCode);*/
		Internet internet=faker.internet();
		String avatar = internet.avatar();
		String image = internet.image();
		System.out.println("avatar--->"+faker.internet().avatar());
		System.out.println("image--->"+image);

		
	}
}
