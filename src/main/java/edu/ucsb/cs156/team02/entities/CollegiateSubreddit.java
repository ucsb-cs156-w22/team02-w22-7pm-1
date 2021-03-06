package edu.ucsb.cs156.team02.entities;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "collegiate_subreddits")
public class CollegiateSubreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

 
    //private User user;
    private String name;
    private String location;
    private String subreddit;
    
}
