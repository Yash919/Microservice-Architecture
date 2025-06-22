package com.lcwd.user.service.UserService.services.impl;

import com.lcwd.user.service.UserService.entities.Hotel;
import com.lcwd.user.service.UserService.entities.Rating;
import com.lcwd.user.service.UserService.entities.User;
import com.lcwd.user.service.UserService.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.UserService.externalService.HotelService;
import com.lcwd.user.service.UserService.repositories.UserRepository;
import com.lcwd.user.service.UserService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUUID = UUID.randomUUID().toString();
        user.setUserId(randomUUID);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll().stream()
                .map(user -> {
                    try {
                        // 1. Fetch ratings for each user
                        Rating[] ratings = restTemplate.getForObject(
                                "http://RATING-SERVICE/ratings/users/" + user.getUserId(),
                                Rating[].class
                        );

                        // 2. Convert array to list and fetch Hotel for each Rating
                        List<Rating> enrichedRatings = Arrays.stream(ratings)
                                .map(rating -> {
                                    try {
                                        ResponseEntity<Hotel> hotelResponse = restTemplate.getForEntity(
                                                "http://HOTEL-SERVICE/hotels/" + rating.getHotelId(),
                                                Hotel.class
                                        );
                                        Hotel hotel = hotelResponse.getBody();
                                        rating.setHotel(hotel);
                                    } catch (Exception ex) {
                                        logger.error("Failed to fetch hotel for ratingId {}: {}", rating.getRatingId(), ex.getMessage());
                                        rating.setHotel(null); // Optional fallback
                                    }
                                    return rating;
                                })
                                .collect(Collectors.toList());

                        // 3. Set ratings to user
                        user.setRatings(enrichedRatings);

                    } catch (Exception e) {
                        logger.error("Failed to fetch ratings for user {}: {}", user.getUserId(), e.getMessage());

                        // ❗️Rethrow exception to trigger @Retry and fallback at controller level
                        throw new RuntimeException("Failed to fetch ratings from RATING-SERVICE", e);
                    }

                    return user;
                })
                .collect(Collectors.toList());
    }


    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User not found with Id: "+userId));
        // Fetch Rating Service from Rating-Service microservices

        Rating[] ratings = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);

        // To see result of ratings
        // logger.info("{}",ratings);
        // user.setRatings(ratings);

        List<Rating> streamRating = Arrays.stream(ratings).toList();

        // To set the hotels inside ratings
        List<Rating> ratingList = streamRating.stream().map(
                rating -> {
                    // API call to hotel service to get the hotel
                    // Below is using RestTemplate we can do via feign client as well
                    // ResponseEntity<Hotel> hotelResponseEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
                    // Hotel hotel = hotelResponseEntity.getBody();
                    // Below is via Feign Client
                    Hotel hotel = hotelService.getHotel(rating.getHotelId());
                    // set the hotel to rating
                    // logger.infor("Response Status code: {} " , hotelResponseEntity.getStatusCode());
                    // return the rating
                    rating.setHotel(hotel);
                    return rating;
                }
        ).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public String deleteUser(String userId) {
        return "";
    }
}
