package com.atticket.product.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.atticket.product.dto.service.GetRemainSeatCntSvcDto;
import com.atticket.product.service.ShowSeatService;
import com.atticket.product.service.ShowService;

public class ShowControllerTest {

	private ShowController showController;

	private ShowSeatService showSeatService = mock(ShowSeatService.class);

	@BeforeEach
	public void setUpTest(){

		showController = new ShowController(showSeatService);
	}


	@Test
	@DisplayName("공연의 남은 좌석수 조회 테스트")
	void getShowsTest() throws Exception {

		//given
		//자리 - 등급 매핑 정보
		given(showSeatService.getRemainSeatCntByShowId(1L)).willReturn(
			Arrays.asList(
				GetRemainSeatCntSvcDto.builder()
					.showId(1L)
					.gradeId(1L)
					.gradeNm("A")
					.seatCnt(5)
					.build(),
				GetRemainSeatCntSvcDto.builder()
					.showId(1L)
					.gradeId(2L)
					.gradeNm("B")
					.seatCnt(3)
					.build()
			)
		);

		Long showId = 1L;

//		mockMvc.perform(get("/shows/" + showId + "/seats/count"))
//			.andExpect(status().isOk())
//			.andExpect(MockMvcResultMatchers.jsonPath("$.data.remainSeats", notNullValue()))
//			.andDo(MockMvcResultHandlers.print());

	//	verify(showSeatService).getRemainSeatCntByShowId(1L);

	}
}
