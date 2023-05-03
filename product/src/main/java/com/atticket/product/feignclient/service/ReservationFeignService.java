package com.atticket.product.feignclient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atticket.product.feignclient.client.ReservationFeignClient;
import com.atticket.product.feignclient.dto.GetReservationSeatsResDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationFeignService {

	private final ReservationFeignClient reservationFeignClient;

	/**
	 * 공연별 예약된 좌석 조회
	 * @param showId
	 * @return
	 */
	public List<Long> getReservationSeats(Long showId) {

		GetReservationSeatsResDto reservationSeats =
			reservationFeignClient.getReservationSeats(String.valueOf(showId))
				.getData();
		return reservationSeats.getSeatIds();
	}

}
