package com.atticket.product.controller;

import static com.atticket.common.response.BaseResponse.ok;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atticket.common.response.BaseResponse;
import com.atticket.product.domain.Grade;
import com.atticket.product.domain.Product;
import com.atticket.product.domain.Show;
import com.atticket.product.dto.request.GetProductsReqDto;
import com.atticket.product.dto.response.GetProductResDto;
import com.atticket.product.dto.response.GetProductsResDto;
import com.atticket.product.dto.response.GetShowsResDto;
import com.atticket.product.service.GradeService;
import com.atticket.product.service.ProductService;
import com.atticket.product.service.ShowService;
import com.atticket.product.type.AgeLimit;
import com.atticket.product.type.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final ShowService showService;
	private final GradeService gradeService;

	/**
	 * 상품 검색
	 */
	@GetMapping("")
	public BaseResponse<GetProductsResDto> getProducts(@ModelAttribute GetProductsReqDto req) {

		log.info("searchProductList - request : " + req);

		return ok(GetProductsResDto.builder().productList(List.of(
			GetProductsResDto.Product.builder()
				.image("https://s3.atticket.com/products/images/cats")
				.id(1L)
				.name("뮤지컬 〈캣츠〉 오리지널 내한－성남（Musical CATS）")
				.place(GetProductsResDto.Place.builder()
					.id(1L)
					.name("성남아트센터")
					.build()
				)
				.startDate(LocalDate.of(2023, 05, 05))
				.endDate(LocalDate.of(2023, 05, 07))
				.runningTime(LocalTime.of(2, 40))
				.interMission(LocalTime.of(0, 20))
				.ageLimit(AgeLimit.EIGHT)
				.category(Category.MUSICAL)
				.build(),
			GetProductsResDto.Product.builder()
				.image("https://s3.atticket.com/products/images/cats")
				.id(2L)
				.name("뮤지컬 〈캣츠〉 오리지널 내한－대전（Musical CATS）")
				.place(GetProductsResDto.Place.builder()
					.id(2L)
					.name("대전예술의전당")
					.build()
				)
				.startDate(LocalDate.of(2023, 05, 19))
				.endDate(LocalDate.of(2023, 05, 21))
				.runningTime(LocalTime.of(2, 40))
				.interMission(LocalTime.of(0, 20))
				.ageLimit(AgeLimit.EIGHT)
				.category(Category.MUSICAL)
				.build(),
			GetProductsResDto.Product.builder()
				.image("https://s3.atticket.com/products/images/cats")
				.id(3L)
				.name("뮤지컬 〈캣츠〉 오리지널 내한－수원（Musical CATS）")
				.place(GetProductsResDto.Place.builder()
					.id(3L)
					.name("경기아트센터")
					.build()
				)
				.startDate(LocalDate.of(2023, 05, 12))
				.endDate(LocalDate.of(2023, 05, 14))
				.runningTime(LocalTime.of(2, 40))
				.interMission(LocalTime.of(0, 20))
				.ageLimit(AgeLimit.EIGHT)
				.category(Category.MUSICAL)
				.build())
		).build());
	}

	/**
	 * 상품 상세 조회
	 */
	@GetMapping("/{productId}")

	public BaseResponse<GetProductResDto> getProduct(@PathVariable("productId") Long id) {

		log.debug("getProduct - id : " + id);

		//상품 정보
		Product product = productService.getProductById(id);

		//상품이 없는 경우
		if (product.getId() == null) {
			return ok(
				GetProductResDto.builder().build()
			);
		}

		//등급 정보
		List<Grade> grades = gradeService.getGradesByProductId(id);

		List<GetProductResDto.Grade> gradeList =
			grades.stream().map(x ->
				GetProductResDto.Grade.builder()
					.price(x.getPrice())
					.type(x.getType())
					.build()
			).collect(Collectors.toList());

		return ok(
			GetProductResDto.builder()
				.product(
					GetProductResDto.Product.builder()
						.category(product.getCategory())
						.subCategory(product.getSubCategory())
						.name(product.getName())
						.explain(product.getExplain())
						.runningTime(product.getRunningTime())
						.startDate(product.getStartDate())
						.endDate(product.getEndDate())
						.ageLimit(product.getAgeLimit())
						.image(product.getImage())
						.region(product.getRegion())
						.build()
				)
				.seatGrades(gradeList)
				.showDates(showService.getShowDatesByProductId(id))
				.build()
		);
	}

	//일자별 공연 조회
	@GetMapping("/{productId}/shows")
	public BaseResponse<GetShowsResDto> getShows(@PathVariable("productId") Long productId,
		@RequestParam("date") String inputDate) throws Exception {

		log.debug("getShowList - productId : " + productId);
		log.debug("getShowList - date : " + inputDate);

		//LocalDate로  입력 날짜 파싱
		LocalDate paredDate = LocalDate.parse(inputDate, DateTimeFormatter.BASIC_ISO_DATE);

		//날짜의 공연 리스트 조회
		List<Show> shows = showService.getShowDateByProductId(productId, paredDate);

		List<GetShowsResDto.Show> showList =
			shows.stream()
				.map(show -> GetShowsResDto.Show.builder()
					.id(show.getId())
					.session(show.getSession())
					.time(show.getTime())
					.build()
				).collect(Collectors.toList());

		return ok(
			GetShowsResDto.builder()
				.shows(showList)
				.build()
		);
	}

	/**
	 * 상품 삭제
	 * */
	@DeleteMapping("/{productId}")
	public BaseResponse deleteProduct(@PathVariable("productId") Long id) {

		productService.deleteProduct(id);
		return ok();
	}

}
