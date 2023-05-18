package com.atticket.product.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.atticket.product.domain.Show;
import com.atticket.product.repository.ShowRepository;

class ShowServiceTest {

	private ShowRepository showRepository = mock(ShowRepository.class);
	private ShowService showService;

	private ShowSeatService showSeatService;

	@BeforeEach
	public void setUpTest() {
		showService = new ShowService(showRepository, showSeatService);
	}

	@Test
	@DisplayName("상품의 공연 정보 리스트 조회")
	void getShowsByProductId() {
		//Given
		Long productId = 1L;
		List<Show> givenShows = Arrays.asList();

		when(showRepository.findShowsByProductId(productId)).thenReturn(givenShows);

		//When
		List<Show> results = showService.getShowsByProductId(productId);

		//Then
		Assertions.assertEquals(givenShows, results);
		verify(showRepository).findShowsByProductId(1L);

	}

}
