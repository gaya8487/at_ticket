package com.atticket.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atticket.product.feignClient.service.ReservationFeignService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {

	private final ReservationFeignService reservationFeignService;

	/***
	 * 공연별 예약된 좌석 조회
	 * @param showId
	 * @return
	 */
	public List<Long> getReservationSeats(Long showId) {
		return reservationFeignService.getReservationSeats(showId);
	}
}
