package com.atticket.product.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Show {

	//공연 Id
	private Long id;
	//공연 시간
	private LocalTime time;
	//공연 회차
	private int session;
	//공연 일자
	private LocalDate date;

	private Long productId;

}
