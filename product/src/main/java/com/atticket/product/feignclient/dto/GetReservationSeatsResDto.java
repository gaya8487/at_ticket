package com.atticket.product.feignclient.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetReservationSeatsResDto {

	private final Long showId;

	private final List<Long> seatIds;

}
