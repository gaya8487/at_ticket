package com.atticket.product.controller;

import static com.atticket.common.response.BaseResponse.ok;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atticket.common.response.BaseResponse;
import com.atticket.product.domain.Show;
import com.atticket.product.domain.ShowSeat;
import com.atticket.product.dto.request.RegisterShowReqDto;
import com.atticket.product.dto.response.GetRemainSeatsCntResDto;
import com.atticket.product.dto.response.GetRemainSeatsResDto;
import com.atticket.product.dto.response.RegisterShowResDto;
import com.atticket.product.repository.GradeRepository;
import com.atticket.product.repository.ShowSeatRepository;
import com.atticket.product.service.ShowSeatService;
import com.atticket.product.service.ShowService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//좌석 조회
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shows")
public class ShowController {

	private final GradeRepository gradeRepository;
	private final ShowSeatRepository showSeatRepository;
	private final ShowService showService;
	private final ShowSeatService showSeatService;

	//공연의 남은 좌석 조회
	@GetMapping("/{showId}/seats")
	public BaseResponse<GetRemainSeatsResDto> getRemainSeats(@PathVariable("showId") String id) {

		return ok(GetRemainSeatsResDto.builder().showSeats(List.of(
			GetRemainSeatsResDto.ShowSeat.builder()

				.id(1L)
				.space("1층")
				.locX("12")
				.locY("22")
				.row("T열")
				.rowNum(1)
				.grade("VIP")
				.price(120000)
				.build(),
			GetRemainSeatsResDto.ShowSeat.builder()
				.id(2L)
				.space("1층")
				.locX("15")
				.locY("25")
				.row("T열")
				.rowNum(2)
				.grade("VIP")
				.price(120000)
				.build(),
			GetRemainSeatsResDto.ShowSeat.builder()
				.id(3L)
				.space("1층")
				.locX("18")
				.locY("28")
				.row("T열")
				.rowNum(3)
				.grade("VIP")
				.price(120000)
				.build()
		)).build());
	}

	/**
	 * 공연의 남은 좌석수 조회
	 * @param showId
	 * @return
	 */
	@GetMapping("/{showId}/seats/count")
	public BaseResponse<GetRemainSeatsCntResDto> getRemainSeatsCnt(@PathVariable("showId") Long showId) {

		log.debug("getRemainSeatsCnt - showId : " + showId);

		//공연의 좌석 - 등급 매핑 정보 조회
		List<ShowSeat> showSeats = showSeatRepository.findShowSeatByShowId(showId);

		//showId로 예매 좌석 리스트 조회
		List<Long> reservationSeats = showService.getReservationSeats(showId);

		if (reservationSeats == null || reservationSeats.size() == 0) {
			return ok(
				GetRemainSeatsCntResDto.builder().build()
			);
		}

		List<GetRemainSeatsCntResDto.RemainSeat> remainSeats = new ArrayList<>();

		//showSeats 등급별 SeatList 카운트
		//등급별 남은 좌석 :showSeats  등급별 좌석  -  예매 좌석
		for (ShowSeat showSeat : showSeats) {
			List<Long> seats = showSeatRepository.convertStringToList(showSeat.getSeatList());
			int remainSeatCnt = seats.size();

			log.debug("showId : " + showId);
			log.debug("seats : " + showSeat.getSeatList());

			for (Long seatId : reservationSeats) {
				for (Long seat : seats) {

					log.debug("reserved seats : " + seatId);
					if (seat.equals(seatId)) {
						remainSeatCnt = remainSeatCnt - 1;
					}
				}
			}

			remainSeats.add(
				GetRemainSeatsCntResDto.RemainSeat.builder()
					.showId(showSeat.getShowId())
					.grade(
						(gradeRepository.findById(showSeat.getGradeId())).get().getType()
					)
					.cnt(remainSeatCnt)
					.build()
			);

		}

		return ok(
			GetRemainSeatsCntResDto.builder()
				.remainSeats(remainSeats)
				.build()
		);
	}

	/**
	 * 공연 등록
	 * @param registerShowReqDto
	 * @param productId
	 * @return
	 */
	@PostMapping("/product/{productId}")
	public BaseResponse<RegisterShowResDto> registerShow(@RequestBody RegisterShowReqDto registerShowReqDto,
		@PathVariable("productId") String productId) {

		log.debug("registerShow - registerShowReqDto : " + registerShowReqDto);
		log.debug("registerShow - productId : " + productId);

		//공연들 등록 ->  return:등록된 공연 Id 리스트
		List<Long> showIds = registerShowReqDto.getShows()
			.stream()
			.map(x -> showService.registerShow(
					Show.builder()
						.session(x.getSession())
						.time(x.getTime())
						.date(x.getDate())
						.hallId(x.getHallId())
						.build(),
					registerShowReqDto.getProductId()
				)

			)
			.collect(Collectors.toList());

		//등록된 공연 id로 좌석-등급 매핑 저장
		for (Long id : showIds) {
			registerShowReqDto.getShows()
				.forEach(x ->
					x.getSeats().stream()
						.forEach(
							seat -> showSeatService.registerShowSeat(
								ShowSeat.builder()
									.showId(id)
									.gradeId(seat.getGradeId())
									.productId(Long.valueOf(productId))
									.seatList(ShowSeatRepository.convertListToString(seat.getSeatIds())
									).build()
							)
						)
				);
		}

		//등록 공연 id 리턴
		return ok(
			RegisterShowResDto.builder()
				.showIds(showIds)
				.build()
		);
	}

}
