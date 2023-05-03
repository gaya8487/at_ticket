package com.atticket.product.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterShowReqDto {

	//상품 Id
	private Long productId;
	//공연 리스트
	private List<Show> shows;

	@Getter
	@ToString
	public static class Show {

		//공연 회차
		private int session;

		//공연 시간
		private LocalTime time;

		//공연 날짜
		private LocalDate date;

		//공연 홀
		private Long hallId;

		//좌석 정보 (좌석ID, 등급)
		private List<Seat> seats;
	}

	@Getter
	@ToString
	public static class Seat {

		//좌석 등급 id
		private Long gradeId;

		//좌석 Id 리스트
		private List<Long> seatIds;

	}
}
