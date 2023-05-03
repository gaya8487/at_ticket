package com.atticket.product.service;

import org.springframework.stereotype.Service;

import com.atticket.product.domain.ShowSeat;
import com.atticket.product.repository.ShowSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowSeatService {
	private final ShowSeatRepository showSeatRepository;

	public Long registerShowSeat(ShowSeat showSeat) {

		//Todo 동일한 show Id, session Id가 이미 등록되어 있나 체크

		return showSeatRepository.save(showSeat);
	}

}
