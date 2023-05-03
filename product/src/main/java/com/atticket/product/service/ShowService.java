package com.atticket.product.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.atticket.common.response.BaseException;
import com.atticket.common.response.BaseStatus;
import com.atticket.product.domain.Show;
import com.atticket.product.dto.service.RegisterShowServiceDto;
import com.atticket.product.repository.ShowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {

	private final ShowRepository showRepository;
	private final ShowSeatService showSeatService;

	/**
	 * 상품의 공연 정보 리스트 조회
	 * @param productId
	 * @return
	 */
	public List<Show> getShowsByProductId(Long productId) {
		return showRepository.findShowsByProductId(productId);
	}

	/**
	 * 상품의 공연 날짜 리스트 조회
	 * @param productId
	 * @return
	 */
	public List<LocalDate> getShowDatesByProductId(Long productId) {
		return showRepository.findShowsByProductId(productId)
			.stream()
			.map(Show::getDate)
			.collect(Collectors.toList());
	}

	/**
	 * 특정 날짜의 공연 리스트 조회
	 * @param productId
	 * @param date
	 * @return
	 */
	public List<Show> getShowDateByProductId(Long productId, LocalDate date) {
		return showRepository.findShowsByProductId(productId)
			.stream()
			.filter(show -> show.getDate().equals(date))
			.collect(Collectors.toList());
	}

	/**
	 * 공연 등록
	 * @param productId
	 * @param RegisterShowDto
	 * @return 등록된 showId
	 */
	public Long registerShow(long productId, RegisterShowServiceDto RegisterShowDto) {

		LocalDate paredDate;
		LocalTime paredtime;
		Long registedShowId = 0L;

		try {
			paredDate = LocalDate.parse(RegisterShowDto.getDate(), DateTimeFormatter.BASIC_ISO_DATE);
			paredtime = LocalTime.parse(RegisterShowDto.getTime(), DateTimeFormatter.BASIC_ISO_DATE);
		} catch (Exception e) {
			throw new BaseException(BaseStatus.UNEXPECTED_ERROR);
		}

		Show show = Show.builder()
			.productId(productId)
			.date(paredDate)
			.time(paredtime)
			.session(Integer.parseInt(RegisterShowDto.getSession()))
			.hallId(Long.parseLong(RegisterShowDto.getHallId()))
			.build();

		registedShowId = showRepository.save(show, productId);

		//등록된 공연의 좌석-등급 매핑 저장
		//showId에 해당하는 sessionId가 이미 저장되어있으면 덮어쓰기

		showSeatService.registerShowSeat();

		return registedShowId;
	}

}
