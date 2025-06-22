package com.lcwd.user.service.UserService.externalService;

import com.lcwd.user.service.UserService.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

    @PostMapping("/ratings")
    public Rating createRating(Rating values);

    // In future Enhancement
    @PutMapping("/rating/{ratingId}")
    public Rating updateRating(@PathVariable("ratingId") String ratingId, Rating rating);

    // In future Enhancement
    @DeleteMapping("/rating/{ratingId}")
    public void deleteRating(@PathVariable String ratingId);


}
