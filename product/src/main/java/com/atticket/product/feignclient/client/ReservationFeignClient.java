package com.atticket.product.feignclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.atticket.common.response.BaseResponse;
import com.atticket.product.feignclient.dto.GetReservationSeatsResDto;

@FeignClient(name = "reservationClient", url = "localhost:9000/reservations")
public interface ReservationFeignClient {
	@GetMapping(value = "/show/{showId}/seats")
	BaseResponse<GetReservationSeatsResDto> getReservationSeats(@PathVariable("showId") String showId);
}
