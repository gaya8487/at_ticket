package com.atticket.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atticket.product.domain.Show;
import com.atticket.product.feignclient.service.ReservationFeignService;
import com.atticket.product.repository.ShowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {

	private final ReservationFeignService reservationFeignService;
	private final ShowRepository showRepository;

	/***
	 * 공연별 예약된 좌석 조회
	 * @param showId
	 * @return
	 */
	public List<Long> getReservationSeats(Long showId) {
		return reservationFeignService.getReservationSeats(showId);
	}

	public Long registerShow(Show show, Long productId) {

		//Todo 동일한 show Id, session Id가 이미 등록되어 있나 체크

		return showRepository.save(show, productId);
	}

}
