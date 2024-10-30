package aimo.backend.domains.post.model;

public enum Category {

	COMMON("일반"),
	MBTI("MBTI"),
	MARRIAGE("결혼"),
	LOVE("연애"),
	FOOD("음식"),
	DAILY("일상"),
	PARENTING("육아"),
	SPORTS("스포츠"),
	HOBBY("취미"),
	INVEST("투자"),
	MONEY("돈"),
	ART("예술");

	private final String value;

	Category(String value) {
		this.value = value;
	}
}
