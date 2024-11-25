package com.team1.cineBox.user;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWatchedVideoList {

	List<Long> movieList = new ArrayList <>();
	List<Long> showList = new ArrayList<>();

}
